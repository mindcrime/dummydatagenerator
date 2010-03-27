package datagen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class DataGenMain
{
	protected ArrayList<String> lastNames = new ArrayList<String>();
	protected ArrayList<String> firstNamesF = new ArrayList<String>();
	protected ArrayList<String> firstNamesM = new ArrayList<String>();
	protected ArrayList<ZipStateCityData> USAZipStateCities = new ArrayList<ZipStateCityData>();
	protected ArrayList<ZipStateCityData> CNZipStateCities = new ArrayList<ZipStateCityData>();
	protected ArrayList<String> streetNames = new ArrayList<String>();
	protected ArrayList<String> streetSuffixes = new ArrayList<String>();
	
	protected Random maleOrFemale;
	protected Random usaOrCanada;
	protected Random lastNamesRand;
	protected Random firstnamesFRand;
	protected Random firstNamesMRand;
	protected Random zipStateCityRand;
	protected Random streetNumberRand;
	protected Random streetNameRand;
	protected Random phoneNumberRand;
	
	
	private static int BAD_COMMAND_LINE = -1;
	
	private int numberToGenerate = 0;
	private String outputFormat = "csv"; // TODO: make an enum for this
	private boolean generateAddresses = false;
	private boolean generateEmails = false;
	private boolean generatePhoneNums = false;
	
	public static void main( String[] args ) throws Exception
	{
		if( null == args || args.length < 1 )
		{
			printUsageInstructions();
			System.exit( DataGenMain.BAD_COMMAND_LINE );
		}
		
		Map<String, String> paramsMap = null;
		for( String arg : args )
		{
			if( "-prompt".equalsIgnoreCase( arg ))
			{
				paramsMap = runInteractiveInterface();
			}
		}
		if( null == paramsMap )
		{
			paramsMap = generateParamsMapFromCommandLine( args );
		}
		
		DataGenMain main = new DataGenMain(paramsMap);
		main.run();
	}

	private static void printUsageInstructions()
	{
		System.out.println( "java -cp <classpathspec> datagen.DataGenMain { [-prompt] -n <number> } [-o <outputspec>\n");
		System.out.println( "-prompt: -prompt will cause the program to prompt interactively for things like 'how many records do you want?' etc." 
							+ " All other command line arguments are ignored with -prompt" );
		System.out.println( "-n: If -prompt is NOT specified, -n <number> is the minimum required command line.  <number> is the number of records you want to create." );
		System.out.println( "-o: -o <outputspec> controls the output format.  Defaults to csv if not specified.  Valid options are csv and TBD");
		System.out.println( "-address: -address is a boolean flag which, if present, turns on generation of address data.");
		System.out.println( "-email: -email is a boolean flag which, if present, turns on generation of email addresses.");
		System.out.println( "-phone: -phone is a boolean flag which, if present, turns on generation of phone numbers.");
	}
	
	private static Map<String, String> runInteractiveInterface()
	{
		if( true )
		{
			throw new NotImplementedException( "Not implemented yet" );
		}
		Map<String, String> params = new HashMap<String, String>();
		
		return params;		
	}
	
	public static Map<String, String> generateParamsMapFromCommandLine( String[] args )
	{
		Map<String, String> params = new HashMap<String, String>();
		for( int i = 0; i < args.length; i++ )
		{
			String arg = args[i];
			if( "-n".equalsIgnoreCase(arg) )
			{
				String num = args[i+1];
				params.put( "number.to.generate", num );
			}
			else if( "-o".equalsIgnoreCase( arg ))
			{
				String outputSpec = args[i+1];
				params.put( "output.spec", outputSpec );
			}
			else if( "-address".equals( arg ))
			{
				params.put( "generate.addresses", "true" );
			}
			else if( "-email".equals( arg ))
			{
				params.put( "generate.emails", "true" );
			}
			else if( "-phone".equals( arg ))
			{
				params.put( "generate.phones", "true" );	
			}			
		}
		
		return params;
	}
	
	public DataGenMain( Map<String, String> params )
	{
		String numberParam = params.get( "number.to.generate" );
		numberToGenerate = Integer.parseInt( numberParam );
	
		String outputSpecParam = params.get( "output.spec" );
		if( null != outputSpecParam )
		{
			this.outputFormat = outputSpecParam;
		}
		
		String generateAddressParam = params.get( "generate.addresses" );
		if( null != generateAddressParam )
		{
			generateAddresses = Boolean.parseBoolean( generateAddressParam );
		}

		String generateEmailsParam = params.get( "generate.emails" );
		if( null != generateEmailsParam )
		{
			generateEmails = Boolean.parseBoolean( generateEmailsParam );
		}
		
		String generatePhonesParam = params.get( "generate.phones" );
		if( null != generatePhonesParam )
		{
			generatePhoneNums = Boolean.parseBoolean( generatePhonesParam );
		}
		
	}
	
	public void run() throws Exception
	{
		initializeData();
		initializeRandoms();
		
		ArrayList<PersonData> names = new ArrayList<PersonData>(1000);
		
		// now generate unique data elements
		for( int i = 0; i < numberToGenerate; i++ )
		{
			String lastName = null;
			int lastIndex = lastNamesRand.nextInt(lastNames.size()-1);
			lastName = lastNames.get( lastIndex );
			
			String givenName = null;
			boolean female = maleOrFemale.nextBoolean();
			if( female )
			{
				int firstIndexF = firstnamesFRand.nextInt(firstNamesF.size()-1);
				givenName = firstNamesF.get( firstIndexF );
			}
			else
			{
				int firstIndexM = firstNamesMRand.nextInt(firstNamesM.size()-1);
				givenName = firstNamesM.get( firstIndexM );
			}
		
			String name = StringUtils.capitalize( givenName.toLowerCase() ) 
									+ " " + StringUtils.capitalize( lastName.toLowerCase() );
			PersonData elem = new PersonData();
			elem.id = i;
			elem.name = name;
			
			if( generateAddresses )
			{
				boolean usa = usaOrCanada.nextBoolean();
				ZipStateCityData zipStateCity = null;
				if( usa )
				{
					int indexZipStateCity = this.zipStateCityRand.nextInt(USAZipStateCities.size()-1);
					zipStateCity = USAZipStateCities.get( indexZipStateCity );
				}
				else
				{
					int indexZipStateCity = this.zipStateCityRand.nextInt(CNZipStateCities.size()-1);
					zipStateCity = CNZipStateCities.get( indexZipStateCity );				
				}
				
				String city = zipStateCity.city.trim();
				if( city.contains( " " ))
				{
					String[] cityNameParts = city.split( " " );
					String tempCity = "";
					for( String part : cityNameParts )
					{
						tempCity += ( StringUtils.capitalize( part.toLowerCase() ) + " " );
					}
				
					city = tempCity.trim();
				}
				else
				{
					city = StringUtils.capitalize( city.toLowerCase() );
				}
				
				elem.city = city;
				elem.state = zipStateCity.state;
				elem.zip = zipStateCity.zip;
				elem.country = zipStateCity.country;
				
				StringBuffer street = new StringBuffer();
				int streetNumber = streetNumberRand.nextInt(9899) + 100;
				street.append( String.format( "%d", streetNumber ) );
				street.append( " " );
				
				int streetNameIndex = streetNameRand.nextInt( streetNames.size() -1);
				street.append( StringUtils.capitalize( streetNames.get( streetNameIndex ).toLowerCase()) );
				street.append( " " );
				int streetSuffixIndex = streetNameRand.nextInt( streetSuffixes.size() -1);
				street.append( StringUtils.capitalize( streetSuffixes.get( streetSuffixIndex ).toLowerCase() ) );
				elem.street = street.toString();
				
			}
			
			if( generatePhoneNums )
			{
				int nAreaCode = phoneNumberRand.nextInt( 899) + 100;
				String areaCode = String.format( "%d", nAreaCode );
				int nPrefix = phoneNumberRand.nextInt( 899 ) + 100;
				String prefix = String.format( "%d", nPrefix );
				int nPhoneNumber = phoneNumberRand.nextInt( 8999 ) + 1000;
				String phoneNumber = String.format( "%d", nPhoneNumber );
				elem.phone = areaCode + "-" + prefix + "-" + phoneNumber;
			
			}
			
			if( generateEmails )
			{
				elem.email = generateRandomString() + "@example.com";
			}
			
			names.add( elem );
		}
		
		exportData( names );
		
		System.out.println( "done" );		
	}
	
	protected void exportData( List<PersonData> names ) throws Exception
	{
	     CSVWriter writer = new CSVWriter(new FileWriter("dummydata_out.csv") );
	     int numOutputFields = 2;
	     if( generateAddresses)
	     {
	    	 numOutputFields += 5;
	     }
	     if( generateEmails )
	     {
	    	 numOutputFields++;
	     }
	     if( generateEmails )
	     {
	    	 numOutputFields++;
	     }
	     
	     
	     for( PersonData elem : names )
	     {
	    	 String[] next = new String[numOutputFields];
	    	 next[0] = Integer.toString( elem.id );
	    	 next[1] = elem.name;
	    	 int nextField = 2;
	    	 if( generateAddresses)
	    	 {
	    		 next[nextField++] = elem.street;
	    		 next[nextField++] = elem.city;
	    		 next[nextField++] = elem.state;
	    		 next[nextField++] = elem.zip;
	    		 next[nextField++] = elem.country;
	    	 }
	    	 if( generateEmails )
	    	 {
	    		 next[nextField++] = elem.email;
	    	 }
	    	 if( generatePhoneNums )
	    	 {
	    		 next[nextField++] = elem.phone;
	    	 }
	    	 
	    	 writer.writeNext( next );
	     }
	
	     writer.close();
	}
	
	protected void initializeRandoms()
	{
		maleOrFemale 		=	new Random( System.currentTimeMillis() );
		usaOrCanada 		= 	new Random( System.currentTimeMillis() );
		lastNamesRand 		= 	new Random( System.currentTimeMillis() );
		firstnamesFRand 	= 	new Random( System.currentTimeMillis() );
		firstNamesMRand 	= 	new Random( System.currentTimeMillis() );
		zipStateCityRand 	= 	new Random( System.currentTimeMillis() );
		streetNameRand 		= 	new Random( System.currentTimeMillis() );
		streetNumberRand 	= 	new Random( System.currentTimeMillis() );
		phoneNumberRand 	=	new Random( System.currentTimeMillis() );
	}

	protected void initializeData() throws Exception
	{
		initLastNames();
		initFemaleFirstNames();
		initMaleFirstNames();		
		initZipStateCityData();
		initStreetData();
	}

	protected void initLastNames() throws Exception
	{
		FileReader lastNamesReader = new FileReader( "data/names_dist.all.last" );
		BufferedReader lastNamesBR = new BufferedReader( lastNamesReader );
			
		int last = 0;
		for( String line = lastNamesBR.readLine(); line != null; line = lastNamesBR.readLine() )
		{
			last++;
			String[] result = line.split("\\s");
			lastNames.add( result[0] );
		}
	}
	
	protected void initFemaleFirstNames() throws Exception
	{
		FileReader firstNamesFReader = new FileReader( "data/names_dist.female.first" );
		BufferedReader firstNamesFBR = new BufferedReader( firstNamesFReader );
		
		int femaleFirst = 0;
		for( String line = firstNamesFBR.readLine(); line != null; line = firstNamesFBR.readLine() )
		{
			femaleFirst++;
			String[] result = line.split("\\s");
			firstNamesF.add( result[0] );
		}			
	}

	protected void initMaleFirstNames() throws Exception
	{
		FileReader firstNamesMReader = new FileReader( "data/names_dist.male.first" );
		BufferedReader firstNamesMBR = new BufferedReader( firstNamesMReader );

		for( String line = firstNamesMBR.readLine(); line != null; line = firstNamesMBR.readLine() )
		{
			String[] result = line.split("\\s");
			firstNamesM.add( result[0] );
		}		
	}

	protected void initZipStateCityData() throws Exception
	{
		CSVReader reader = new CSVReader(new FileReader("data/us_zip_codes.csv"));
	    String [] nextLine;
	    while ((nextLine = reader.readNext()) != null) 
	    {
	        ZipStateCityData elem = new ZipStateCityData();
	        elem.zip = nextLine[0];
	        elem.city = nextLine[3];
	        elem.state = nextLine[4];
	        elem.country = "USA";
	        USAZipStateCities.add( elem );
	        // System.out.println( elem );
	    }

	    
	    reader = new CSVReader(new FileReader("data/canada_postal_codes.csv"), '|' );
	    while ((nextLine = reader.readNext()) != null) {
	        ZipStateCityData elem = new ZipStateCityData();
	        elem.zip = nextLine[1];
	        elem.city = nextLine[2];
	        elem.state = nextLine[4];
	        elem.country = "CAN";
	        
	        CNZipStateCities.add( elem );
	    }
	    
	}

	protected void initStreetData() throws Exception
	{
		initStreetNames();
		initStreetSuffixes();
	}
	
	protected void initStreetNames() throws Exception
	{
		FileReader streetNamesReader = new FileReader( "data/street_names.txt" );
		BufferedReader streetNamesBR = new BufferedReader( streetNamesReader );
		
		for( String line = streetNamesBR.readLine(); line != null; line = streetNamesBR.readLine())
		{
			streetNames.add( line );
		}	
	}
	
	protected void initStreetSuffixes() throws Exception
	{
		FileReader streetSuffixesReader = new FileReader( "data/street_suffix.txt" );
		BufferedReader streetSuffixesBR = new BufferedReader( streetSuffixesReader );
		
		for( String line = streetSuffixesBR.readLine(); line != null; line = streetSuffixesBR.readLine())
		{
			String[] results = line.split( "\\s" );
			streetSuffixes.add( results[1] );
		}
	}

	protected String generateRandomString() 
	{
		String str=new  String("abcdefghijklmnopqrstuvwzyz0123456789");
	 	StringBuffer sb=new StringBuffer();
	 	Random r = new Random();
	 	int te=0;
	 	for(int i=1;i<=10;i++){
	 		te=r.nextInt(str.length() -1);
	 		sb.append(str.charAt(te));
	 	}
	 	
	 	return sb.toString();
	}
}

class PersonData
{
	public int id;
	public String name;
	public String email;
	public String phone;
	public String street;
	public String city;
	public String state;
	public String zip;
	public String country;

	@Override
	public String toString()
	{
		return "name: " + name + "\n" 
				+ "email: " + email + "\n"
				+ "phone: " + phone + "\n"
				+ "street: " + street + "\n" 
				+ "city: " + city + ", state: " + state + ", zip: " 
				+ zip + ", country: " + country + "\n";
	}
}

class ZipStateCityData
{
	public String city;
	public String state;
	public String zip;
	public String country;

	public String toString()
	{
		return "city: " + city + ", state/province: " + state + ", zip: " + zip + ", country: " + country;
	}
}
