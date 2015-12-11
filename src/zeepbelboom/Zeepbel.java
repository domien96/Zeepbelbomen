package zeepbelboom;

/**
 * Created by user on 25/10/2015.
 */
public interface Zeepbel<Key extends Comparable<Key>> extends Iterable<Key>, Comparable<Zeepbel<Key>> {

    Key getWortelSleutel();

    /**
     * Returnt wortel van deze zeepbel.
     * @return
     */
    Node<Key> getWortelNode();

    /**
     * Vervangt wortel van deze zeepbel.
     * @return
     */
    void setWortelNode(Node<Key> root);

    /**
     * Geeft de parent-zeepbel terug.
     * Returns null if this is the parent zeepbel.
     * @return
     */
    Zeepbel<Key> getOuderZeepbel();

    /**
     *
     * @return : true als het aantal nodes STRIKT groter is dan het maximale toegestane aantal nodes.
     */
    boolean zitOverVol();

    /**
     *
     * @return : als er plek is voor een extra node.
     */
    boolean heeftPlaats();

    /**
     * Splits de zeepbel op als volgt:
     * De wortel behoudt zijn kleur.
     * De linkerdeelboom (van de zeepbel) krijgt een nieuwe kleur.
     * De rechterdeelboom behoudt dezelfde kleur.
     * Merk op dat enkel de sleutels binnen deze zeepbel van kleur veranderen!
     * @complexiteit: Het slechste geval is O(k). (k= de zeepbel-constante)
     */
    void splits();

    /**
     * Geeft de kleur weer, deze wordt voorgesteld door een uniek getal.
     */
    int geefKleur();

    /**
     * Aantal elementen in zeepbel.
     */
    int size();

    /**
     * Geeft linkerbroerzeepbel
     */
    Zeepbel<Key> geefLinkerBroerzeepbel();

    /**
     * Geeft rechterbroerzeepbel
     */
    Zeepbel<Key> geefRechterBroerzeepbel();

    /**
     *
     * @return : True indien zeepbel-size >= 2.
     */
    boolean containsMoreThan1Key();

    /**
     * Key binnenin deze zeepbel met de grootste waarde.
     */
    Node<Key> vindMax();

    /**
     * Key binnenin deze zeepbel met de kleinste waarde.
     */
    Node<Key> vindMin();

    /**
     * Balanceert de nodes binnen deze zeepbel zo goed mogelijk.
     */
    void balanceer();
}
