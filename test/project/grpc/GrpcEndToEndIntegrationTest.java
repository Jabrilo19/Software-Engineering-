package project.grpc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import project.ComputationServiceImpl;
import project.DataStorageServiceImpl;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class GrpcEndToEndIntegrationTest {

    @TempDir
    Path tempDir;

    private DataStoreGrpcServer dataStoreServer;
    private ComputeEngineGrpcServer computeServer;
    private GrpcDataStorageServiceClient dataStoreClient;
    private ComputeEngineClient cliClient;

    @BeforeEach
    void setUp() throws Exception {
        dataStoreServer = new DataStoreGrpcServer(0, new DataStorageServiceImpl());
        dataStoreServer.start();

        dataStoreClient = new GrpcDataStorageServiceClient("localhost", dataStoreServer.getPort());
        computeServer = new ComputeEngineGrpcServer(0, dataStoreClient, new ComputationServiceImpl());
        computeServer.start();

        cliClient = new ComputeEngineClient("localhost", computeServer.getPort());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (cliClient != null) {
            cliClient.close();
        }
        if (computeServer != null) {
            computeServer.stop();
        }
        if (dataStoreClient != null) {
            dataStoreClient.close();
        }
        if (dataStoreServer != null) {
            dataStoreServer.stop();
        }
    }

    @Test
    void testEndToEndFileInputAndDefaultDelimiter() throws Exception {
        Path inputFile = tempDir.resolve("input.txt");
        Path outputFile = tempDir.resolve("output.txt");
        Files.writeString(inputFile, "12, 15, 28, 1");

        ComputeResponse response = cliClient.submitFileRequest(
                inputFile.toString(), outputFile.toString(), null);

        Assertions.assertTrue(response.getSuccess());
        Assertions.assertEquals(
                Arrays.asList("2*2*3", "3*5", "2*2*7", "none"),
                response.getFormattedResultsList());
        Assertions.assertEquals("2*2*3,3*5,2*2*7,none", Files.readString(outputFile));
    }

    @Test
    void testEndToEndInlineNumbersAndCustomDelimiter() throws Exception {
        Path outputFile = tempDir.resolve("inline_output.txt");
        List<Integer> numbers = ComputeEngineClient.parseNumbers("10 21 7");

        ComputeResponse response = cliClient.submitInlineRequest(
                numbers, outputFile.toString(), ";");

        Assertions.assertTrue(response.getSuccess());
        Assertions.assertEquals(Arrays.asList("2*5", "3*7", "7"), response.getFormattedResultsList());
        Assertions.assertEquals("2*5;3*7;7", Files.readString(outputFile));
    }

    @Test
    void testMissingInputFileReturnsFailureStatus() {
        Path missingInput = tempDir.resolve("does_not_exist.txt");
        Path outputFile = tempDir.resolve("failed_output.txt");

        ComputeResponse response = cliClient.submitFileRequest(
                missingInput.toString(), outputFile.toString(), ",");

        Assertions.assertFalse(response.getSuccess());
        Assertions.assertTrue(response.getStatusMessage().startsWith("FAILED:"));
    }

    @Test
    void testDataStoreGrpcClientRoundTrip() throws Exception {
        Path inputFile = tempDir.resolve("ds_in.txt");
        Path outputFile = tempDir.resolve("ds_out.txt");
        Files.writeString(inputFile, "4\n9\n");

        List<Integer> readBack = dataStoreClient.readInput(new StorageSource(inputFile.toString()));
        Assertions.assertEquals(Arrays.asList(4, 9), readBack);

        List<PrimeFactorizationResponse> results = Arrays.asList(
                new PrimeFactorizationResponse(Arrays.asList(2, 2)),
                new PrimeFactorizationResponse(Arrays.asList(3, 3)));
        dataStoreClient.writeOutput(new StorageDestination(outputFile.toString(), "|"), results);

        Assertions.assertEquals("2*2|3*3", Files.readString(outputFile));
    }
}