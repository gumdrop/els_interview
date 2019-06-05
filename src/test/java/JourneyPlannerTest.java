import interview_problem.JourneyPlanner;
import org.junit.Before;
import org.junit.*;
import static org.junit.Assert.*;

import java.time.LocalTime;

public class JourneyPlannerTest {

    JourneyPlanner planner;


    @Before
    public void setup(){
        planner = new JourneyPlanner(TestData.TIMETABLE);
    }


    @Test
    public void testDurationBetween2Stations(){
      assertEquals(150, planner.duration("Camborne", "Exeter St Davids", LocalTime.of(9,7), false));
    }

    @Test
    public void testFirstAvailableTrainDuration(){
        assertEquals(159, planner.duration("Camborne", "Exeter St Davids", LocalTime.of(10,23), false));
    }

    @Test
    public void testDurationIncludingWaitTime(){
        assertEquals(56, planner.duration("St Austell", "Par", LocalTime.of(11,01), true));
    }

}
