package io.kameshsampath.vertx.tooling.utils.test;

import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Set;

import io.kameshsampath.vertx.tooling.utils.ListenPortScannerUtil;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author kameshs
 */
public class ListenPortScannerTest {


    ListenPortScannerUtil listenPortScannerUtil = new ListenPortScannerUtil();

    @Test
    public void tesListenPort() throws Exception {

        File baseDir = Paths.get(this.getClass().getResource("/").toURI()).toFile();

        Set<String> ports = listenPortScannerUtil.getListenPorts(baseDir, "**/*.java", null);

        assertFalse(ports.isEmpty());

        assertThat(ports, hasItems("8040", "8080"));

    }

    @Test
    public void testNoListenPorts() throws Exception {

        File baseDir = Paths.get(this.getClass().getResource("/").toURI()).toFile();

        Set<String> ports = listenPortScannerUtil.getListenPorts(baseDir, "**/*.txt", null);

        assertTrue(ports.isEmpty());

    }
}
