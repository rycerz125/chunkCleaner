package yaml;

import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;
import rycerz125.chunkcleaner.ChunkCleaner;
import rycerz125.chunkcleaner.utils.IntVector;
import rycerz125.chunkcleaner.utils.Utils;
import rycerz125.chunkcleaner.data.ChunkInfo;
import rycerz125.chunkcleaner.data.WorldDataService;

import java.io.File;
import java.util.List;

public class WorldDataServiceTest {

    @Test
    void chunkModifiedTest(){
        WorldDataService worldDataService = new WorldDataService();
        Assertions.assertFalse(worldDataService.chunkModifiedInCurrentSession("w", new IntVector(2,3)));
        worldDataService.setChunkModified("world", new IntVector(2,3));
        Assertions.assertTrue(worldDataService.chunkModifiedInCurrentSession("world", new IntVector(2,3)));
        Assertions.assertFalse(worldDataService.chunkModifiedInCurrentSession("world", new IntVector(2,4)));
        Assertions.assertFalse(worldDataService.chunkModifiedInCurrentSession("w", new IntVector(2,3)));
    }
    @Test
    void saveChunkInfoTest(){
        WorldDataService worldDataService = new WorldDataService();
        worldDataService.saveChunkInfo("swiat", 1,2, 1235);
        worldDataService.saveChunkInfo("swiat", 1,3, 1234255);
        worldDataService.saveChunkInfo("swiat", 1,4, 1234265);
        worldDataService.saveChunkInfo("swiat", 1,5, 1234275);
        worldDataService.saveChunkInfo("swiat", 1,6, 1234285);
        worldDataService.saveChunkInfo("swiat", 1,7, 1234295);
    }

    @Test
    void loadRegionTxt(){
        WorldDataService worldDataService = new WorldDataService();
        ChunkCleaner.utils = new Utils();
        List<ChunkInfo> chunkInfoList = worldDataService.getExistingDataOfRegion("swiat",0,0);
        Assertions.assertEquals(6,chunkInfoList.size());
        Assertions.assertEquals(1,chunkInfoList.get(3).getX());
        Assertions.assertEquals(5,chunkInfoList.get(3).getZ());
        Assertions.assertEquals(1234275,chunkInfoList.get(3).getGenerateTime());
    }

    @Test
    void reduceRegionIslandsTest(){
        WorldDataService worldDataService = new WorldDataService();
        ChunkCleaner.utils = new Utils();
        File file = new File("D:\\842866.wom\\parker_original1.8\\region");
        System.out.println("regiony: "+file.listFiles().length);
        Assertions.assertTrue(file.listFiles().length != 0);

        long time = System.currentTimeMillis();
        List<IntVector> particularRegions = worldDataService.reduceRegionList(
                worldDataService.getAllRegionIndexesOfDirectory(file), 5);
        System.out.println( "operacja zajela "+(System.currentTimeMillis() - time) +" ms");
        for(IntVector intVector : particularRegions){
            System.out.println(intVector.x*512 + "x" + intVector.z*512);
        }
    }

//    @Test
//    void loadRegionDataFromYamlTest(){
//        WorldDataService worldDataService =  new WorldDataService();
//        File file = new File(WorldDataService.getMcaTxtFileDirectory("world", 100, 0));
//
//
//        Yaml yaml = new Yaml(new Constructor(Region.class));
//        InputStream inputStream = this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("dataBase\\chunkCleaner\\world\\r.100.0.mca.yaml");
//        Region region = yaml.load(inputStream);
//        worldDataService.getExistingDataOfRegion("world", 100,0);
//    }
//    @Test
//    void pierwszyPrzyklad(){
//        Yaml yaml = new Yaml();
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(new File("yaml/customer.yaml"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> obj = yaml.load(inputStream);
//        System.out.println(obj);
//    }
//    @Test
//    public void
//    whenLoadYAMLDocumentWithTopLevelClass_thenLoadCorrectJavaObjectWithNestedObjects() {
//
//        Yaml yaml = new Yaml(new Constructor(Customer.class));
//        InputStream inputStream = null;
//        Customer customer = null;
//        try {
//            inputStream = new FileInputStream(new File("yaml/customer_with_contact_details_and_address.yaml"));
//            customer= yaml.load(inputStream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        assertNotNull(customer);
//        assertEquals("John", customer.getFirstName());
//        assertEquals("Doe", customer.getLastName());
//        assertEquals(31, customer.getAge());
//        assertNotNull(customer.getContactDetails());
//        assertEquals(2, customer.getContactDetails().size());
//
//        assertEquals("mobile", customer.getContactDetails()
//                .get(0)
//                .getType());
//        assertEquals(123456789, customer.getContactDetails()
//                .get(0)
//                .getNumber());
//        assertEquals("landline", customer.getContactDetails()
//                .get(1)
//                .getType());
//        assertEquals(456786868, customer.getContactDetails()
//                .get(1)
//                .getNumber());
//        assertNotNull(customer.getHomeAddress());
//        assertEquals("Xyz, DEF Street", customer.getHomeAddress()
//                .getLine());
//    }
//    public class Customer {
//        private String firstName;
//        private String lastName;
//        private int age;
//        private List<Contact> contactDetails;
//        private Address homeAddress;
//
//        public Customer(){
//
//        }
//        public String getFirstName() {
//            return firstName;
//        }
//
//        public String getLastName() {
//            return lastName;
//        }
//
//        public int getAge() {
//            return age;
//        }
//
//        public List<Contact> getContactDetails() {
//            return contactDetails;
//        }
//
//        public Address getHomeAddress() {
//            return homeAddress;
//        }
//    }
//    public class Contact {
//        private String type;
//        private int number;
//        // getters and setters
//
//        public String getType() {
//            return type;
//        }
//
//        public int getNumber() {
//            return number;
//        }
//    }
//    public class Address {
//        private String line;
//        private String city;
//        private String state;
//        private Integer zip;
//        // getters and setters
//
//
//        public String getLine() {
//            return line;
//        }
//    }
}
