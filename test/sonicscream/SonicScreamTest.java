/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonicscream;

import sonicScream.SonicScream;
import javafx.stage.Stage;
import org.junit.*;
import static org.junit.Assert.*;
import sonicscream.utilities.JavaFXThreadingRule;

/**
 *
 * @author Neil McAlister
 */
public class SonicScreamTest
{
    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
    
    public SonicScreamTest()
    {         
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of start method, of class SonicScream.
     */
    @Test
    public void testStart() throws Exception
    {
        System.out.println("start");
        Stage stage = null;
        SonicScream instance = new SonicScream();
        instance.start(stage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class SonicScream.
     */
    @Test
    public void testMain()
    {
        System.out.println("main");
        String[] args = null;
        SonicScream.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
