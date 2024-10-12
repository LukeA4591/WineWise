package seng202.team7.unittests.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.models.Position;
public class PositionTest {

    Position position;

    @BeforeEach
    public void makePositions() {
        position = new Position(70, -100);
    }

    @Test
    public void testToString() {
        String positionString = position.toString();
        Assertions.assertEquals("{\"lat\": 70.0, \"lng\": -100.0}", positionString);
    }
}
