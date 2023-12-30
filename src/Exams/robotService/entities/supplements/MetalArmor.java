package robotService.entities.supplements;

public class MetalArmor extends BaseSupplement{
    private static final double PRICE = 15;
    private static final int HARDNESS = 5;
    public MetalArmor() {
        super(HARDNESS, PRICE);
    }
}
