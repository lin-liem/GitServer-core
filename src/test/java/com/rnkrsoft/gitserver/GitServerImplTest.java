package com.rnkrsoft.gitserver;

import com.rnkrsoft.gitserver.enums.PermissionEnum;
import com.rnkrsoft.gitserver.exception.RepositoryCreateFailureException;
import com.rnkrsoft.gitserver.exception.UninitializedGitServerException;
import com.rnkrsoft.gitserver.http.loader.DefaultFileLoader;
import com.rnkrsoft.util.PasswordUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.junit.Test;

import java.io.IOException;

public class GitServerImplTest {
    @Test
    public void start() throws IOException, InterruptedException, UninitializedGitServerException {
        GitServer gitServer = GitServerFactory.getInstance();
        gitServer.init(GitServerSetting.builder().repositoriesHome("./target").sshPort(8022).httpPort(8080).fileLoader(new DefaultFileLoader()).build()).startup();
        gitServer.registerUser("test", "test", PasswordUtils.generateSha1("123456"));
        gitServer.grantPermission("demo", "test", PermissionEnum.CREATE_REPOSITORY);
        Thread.sleep(600 * 1000);
    }

    @Test
    public void openRepository() throws RepositoryNotFoundException, UninitializedGitServerException {
        GitServerImpl gitServer = new GitServerImpl();
        Git git = gitServer.init(GitServerSetting.builder().repositoriesHome("./target").sshPort(8022).httpPort(8080).fileLoader(new DefaultFileLoader()).build()).openRepository("demo");
        git.toString();
    }

    @Test
    public void createRepository() throws RepositoryCreateFailureException, UninitializedGitServerException {
        GitServerImpl gitServer = new GitServerImpl();
        gitServer.init(GitServerSetting.builder().repositoriesHome("./target").sshPort(8022).httpPort(8080).fileLoader(new DefaultFileLoader()).build()).createRepository("demo", "");
    }

    @Test
    public void renameRepository() throws RepositoryNotFoundException, UninitializedGitServerException {
        GitServerImpl gitServer = new GitServerImpl();
        gitServer.renameRepository("demo", "demo1");
    }

    @Test
    public void deleteRepository() throws RepositoryNotFoundException, UninitializedGitServerException {
        GitServerImpl gitServer = new GitServerImpl();
        gitServer.deleteRepository("demo");
    }
}