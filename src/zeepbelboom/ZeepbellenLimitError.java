package zeepbelboom;

/**
 * Wordt opgeroepen indien er teveel zeepbellen aanwezig zijn.
 * Created by user on 15/11/2015.
 */
public class ZeepbellenLimitError extends Error {
    private static final long serialVersionUID = 42L;

    public ZeepbellenLimitError() {
        super();
    }

    public ZeepbellenLimitError(String msg) {
        super(msg);
    }
}
