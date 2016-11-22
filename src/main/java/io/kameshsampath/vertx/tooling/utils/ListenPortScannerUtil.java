package io.kameshsampath.vertx.tooling.utils;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.FileUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author kameshs
 */
public class ListenPortScannerUtil {

    public static final String PATTERN_ONLY_LISTEN = "^.*\\.listen\\((?<port>\\d{4,10}),?.*\\);$";

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenPortScannerUtil.class);

    public ListenPortScannerUtil() {

    }

    public Set<String> getListenPorts(File baseDir, String includes, String excludes) throws IOException {

        LOGGER.info("Scanning files in dir : {}  with includes [ {} ] and excludes [ {} ]",
                baseDir, includes, excludes);

        Set<String> listenPorts = new HashSet<>();

        List<String> javaSourceFiles = FileUtils.getFileNames(baseDir, includes, excludes, true, true);

        LOGGER.debug("Found files: {}", javaSourceFiles);

        javaSourceFiles.stream().forEach(fileName -> {
            Path path = Paths.get(fileName);

            if (Files.exists(path)) {

                File vertxFileSource = Paths.get(fileName).toFile();

                try {

                    JavaClassSource javaSource = Roaster.parse(JavaClassSource.class, vertxFileSource);

                    LOGGER.trace("Parsed file : {}", javaSource);

                    List<MethodSource<JavaClassSource>> methodSources = javaSource.getMethods();

                    listenPorts.addAll(methodSources.stream()
                            .map(this::findPort)
                            .filter(s -> s.isPresent())
                            .map(Optional::get)
                            .collect(Collectors.toSet()));

                } catch (FileNotFoundException e) {
                    //ignore this can never happen
                }
            }
        });

        LOGGER.debug("Found Ports {} ", listenPorts);

        return listenPorts;
    }

    private Optional<String> findPort(MethodSource<JavaClassSource> methodSource) {

        String port = null;

        String methodBody = StringUtils.trim(methodSource.getBody());

        Pattern pattern = Pattern.compile(PATTERN_ONLY_LISTEN, Pattern.MULTILINE);

        Matcher matcher = pattern.matcher(methodBody);

        if (matcher.find()) {
            port = matcher.group("port");
        }

        return Optional.ofNullable(port);
    }

}
