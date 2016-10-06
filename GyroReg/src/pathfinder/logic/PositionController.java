package pathfinder.logic;

/**
 *
 * @author uli
 */
public class PositionController {

    private static int[] position = new int[]{0, 0};

    public static void update() {
        position[0] = 0;
        position[1] = 0;
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public static int[] getPosition() {
        return position;
    }

    public static float getAngle() {
        return 0f;
    }

}
