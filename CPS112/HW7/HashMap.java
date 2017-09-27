import java.util.ArrayList;

/**
 * This HashMap class is a more general implementation
 * of the BasicDictionary class we developed in class and lab.
 * Here are some of the key difference
 *
 * 1. The type for the key and vaulue type is Object
 *
 * 2. Underlying data structures are ArrayLists instead of arrays
 *
 * 3. No longer does iteration
 *
 * 4. The following methods have been removed
 *    remove (so no checking for "FREE" needed)
 *    printKeysArray
 *    printValuesArray    
 *
 * 5. Added getKeyValues method (see method for a description)
 *
 * 6. Added toString method
 *
 * 7. hash method calls keys hashCode method
 *    All objects have one by default
 */
class HashMap
{
    private static final double LOAD = 2.0/3.0;
    
    private int numKeys;
    protected int hashSize;

    protected ArrayList keys;
    protected ArrayList values;

    public HashMap(int initSize)
    {
	numKeys = 0;
	hashSize = initSize;

	keys = new ArrayList(hashSize);
	values = new ArrayList(hashSize);

	for (int i = 0; i < hashSize; i++)
	{
	    keys.add(null);
	    values.add(null);
	}
    }

    /**
     * Empty constructor, defaults to 
     * HashMap of size of 10
     */
    public HashMap()
    {
	this(10);
    }

    public void put(Object key, Object value)
    {
	int idx = hash(key);

	if (keys.get(idx) == null)
	{
	    keys.set(idx, key);
	    values.set(idx, value);
	}
	else if (keys.get(idx).equals(key))
	    values.set(idx, value); // Replace value for key
	else
	{
	    boolean foundSlot = false;
	    idx = rehash(idx);
	    while(!foundSlot)
	    {
		if (keys.get(idx) == null)
		    foundSlot = true;
		else if (keys.get(idx).equals(key))
		    foundSlot = true;
		else
		    idx = rehash(idx);
	    }

	    keys.set(idx, key);	     
	    values.set(idx, value);
	}

	numKeys++;
	
	/* Resize the table if necessary */
	double tableLoad = (double) numKeys / (double) hashSize;
	if (tableLoad >= LOAD)
	    resize();
    }

    public Object get(Object key)
    {
	int idx = hash(key);
	boolean foundKey = false;
	
	while(!foundKey)
        {
	    if (keys.get(idx) == null)
		return null; // Key not in hashmap
	    else if (keys.get(idx).equals(key))
		foundKey = true;
	    else
		idx = rehash(idx);
	}
	
	return values.get(idx);
    }

    public boolean containsKey(Object key)
    {
	int idx = hash(key);
	boolean foundKey = false;
	
	while(!foundKey)
        {
	    if (keys.get(idx) == null)
		return false;
	    else if (keys.get(idx).equals(key))
		foundKey = true;
	    else
		idx = rehash(idx);
	}
	
	return foundKey;
    }

    /**
     * Creates a list of key/value pairs.  
     * Each key/value is stored in a Pair data structure (see Pair.java)
     *
     * @ return ArrayList of Pairs
     *          Each Pair is a key/value pair
     */
    public ArrayList<Pair<Object, Object>> getKeyValues()
    {
	ArrayList<Pair<Object, Object>> keyValues = new ArrayList<Pair<Object, Object>>();

	for (int i = 0; i < keys.size(); i++)
	{
	    if (keys.get(i) != null)
	    {
		Pair<Object, Object> p = new Pair<Object, Object>(keys.get(i), values.get(i));
		keyValues.add(p);
	    }
	}
	return keyValues;
    }

    @Override
    public String toString()
    {
	String s = "{";

	ArrayList<Pair<Object, Object>> keyValues = getKeyValues();
	for (int i = 0; i < keyValues.size() - 1; i++)
	{
	    Pair<Object, Object> p = keyValues.get(i);
	    s = s + p.x.toString() + ":" + p.y.toString() + ", " ;
	}

	if (keyValues.size() == 0)
	    s =  s + "}";
	else // Add last pair
	{
	    Pair<Object, Object> p = keyValues.get(keyValues.size() - 1);
	    s = s + p.x.toString() + ":" + p.y.toString() + "}";
	}

	return s;
    }
	    
    /**
     * Basic hash function
     * Because key is not primitive
     * can call hashCode() method
     */
    protected int hash(Object key)
    {
	return key.hashCode() % hashSize;
    }

    /**
     * Rehash based on linear probing
     * This only gets called after hash
     * so it is safe to assume input parameter
     * is an int.
     */
    protected int rehash(int oldHash)
    {
	return (oldHash + 1) % hashSize;
    }

    /**
     * Resize HashMap to be twice as big + 1
     */
    private void resize()
    {
        ArrayList oldKeys = new ArrayList(numKeys);
	ArrayList oldValues = new ArrayList(numKeys);

	// Copy old values
	for (int i = 0; i < keys.size(); i++)
	{
	    if (keys.get(i) != null)
	    {
		oldKeys.add(keys.get(i));
		oldValues.add(values.get(i));
	    }
	}

	numKeys = 0;
	hashSize = hashSize * 2  + 1;
	
	keys = new ArrayList(hashSize);
	values = new ArrayList(hashSize);
    
	for (int i = 0; i < hashSize; i++)
	{
	    keys.add(null);
	    values.add(null);
	}
	
	for (int i = 0; i < oldKeys.size(); i++)
	    this.put(oldKeys.get(i), oldValues.get(i));
    }

 public static void main(String[] args)
    {
        ChainingHashMap map = new ChainingHashMap(10);

	
        map.put('A', "Hello");
        map.put('B', "Class");
        map.put('C', "Let's");
        map.put('D', "Chain");
	
	map.put("Candy",new Integer(65));

	System.out.println("Initial hashmap state");
	
	System.out.println("Test getKeyValues: "+ map.getKeyValues());
	/*
        System.out.println("Test get: A --"+ map.get('A'));

	System.out.println("Test containsKey: contains 'E': "+ map.containsKey('E'));
	System.out.println("Test containsKey: contains 'D': "+ map.containsKey('D'));

	int ascii = (int) 'A' + (int) 'B';
	int result = ascii % 10;// test's hashsize is 10
	System.out.println("Test hash: hash\"AB\": "+ map.hash("AB") + "(should be same as) "+ result);
	*/
    }

}

class ChainingHashMap extends HashMap
{
    public ChainingHashMap(int size)
    {
	super(size);
    }

    @Override
    public void put(Object key, Object value)
    {
	int idx = hash(key);

	if (keys.get(idx) == null)
	    {
		ArrayList keychain = new ArrayList();
		keychain.add(key);
		ArrayList valuechain = new ArrayList();
		valuechain.add(value);

		keys.set(idx, keychain);
		values.set(idx, valuechain);
	    }
	else
	    { 
		ArrayList keychain = (ArrayList)keys.get(idx);
		ArrayList valuechain = (ArrayList)values.get(idx);
		if (keychain.contains(key))
		    {
			int index = keychain.indexOf(key);
			valuechain.set(index,value);
			values.set(idx,valuechain);}
		else
		    {
			keychain.add(key);
			keys.set(idx,keychain);   
			valuechain.add(value);
			values.set(idx,valuechain);
		    }
	    }
    }
    
    @Override
    public Object get(Object key)
    {
	int idx = hash(key);
	
	if (keys.get(idx) == null)
	    return null; 
	else 
	    {
		ArrayList keysC = (ArrayList) keys.get(idx);
		ArrayList valuesC = (ArrayList) values.get(idx);
		if (keysC.contains(key))
		    {int index = keysC.indexOf(key);
			return valuesC.get(index);
		    }
		else
		    return null;
	    }
    }

    @Override
    public boolean containsKey(Object key)
    {
	int idx = hash(key);
	boolean foundKey = false;
	if (keys.get(idx) != null)
	    { ArrayList keysC = (ArrayList) keys.get(idx);
		if (keysC.contains(key))
		    foundKey = true;
	    }
	return foundKey;}
    
    @Override 
    public ArrayList<Pair<Object, Object>> getKeyValues()
    {
	ArrayList<Pair<Object, Object>> keyValues = new ArrayList<Pair<Object, Object>>();

	for (int i = 0; i < keys.size(); i++)
	{
	    if (keys.get(i) != null)
		{
		    //System.out.println(keys.get(i).toString());
		    ArrayList keysC = (ArrayList) keys.get(i);
		    ArrayList valuesC = (ArrayList) values.get(i);
		    for (int j = 0; j<keysC.size();j++)
			{Pair<Object, Object> p = new Pair<Object, Object>(keysC.get(j), valuesC.get(j));
			    keyValues.add(p);}
		}
	}
	return keyValues;
    }

    @Override 
    public int hash(Object key)
    {
	//System.out.println(key.toString() + " check!");
	String theKey = key.toString();
	int result = 0;
	for (int i = 0; i<theKey.length();i++)
	    result+= (int) theKey.charAt(i);
	return result % hashSize;
    }


}