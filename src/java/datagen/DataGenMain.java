package datagen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

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
	
	public static void main( String[] args ) throws Exception
	{
		DataGenMain main = new DataGenMain();
		main.run();
	}

	public void run() throws Exception
	{
		initializeData();
		initializeRandoms();
		
		ArrayList<PersonData> names = new ArrayList<PersonData>(1000);
		
		// now generate 1000 unique data elements
		for( int i = 0; i < 5 /*1000 */ ; i++ )
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
			
			// TODO: add a command line argument to toggle this
			// if( generateAddressData )
			// {
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
			street.append( String.format( "%4d", streetNumber ) );
			street.append( " " );
			
			int streetNameIndex = streetNameRand.nextInt( streetNames.size() -1);
			street.append( StringUtils.capitalize( streetNames.get( streetNameIndex ).toLowerCase()) );
			street.append( " " );
			int streetSuffixIndex = streetNameRand.nextInt( streetSuffixes.size() -1);
			street.append( StringUtils.capitalize( streetSuffixes.get( streetSuffixIndex ).toLowerCase() ) );
			elem.street = street.toString();
			
			// }
			
			// TODO: put in a command line argument to toggle this
			// if( generatePhoneNumbers )
			// {
			int nAreaCode = phoneNumberRand.nextInt( 899) + 100;
			String areaCode = String.format( "%d", nAreaCode );
			int nPrefix = phoneNumberRand.nextInt( 899 ) + 100;
			String prefix = String.format( "%d", nPrefix );
			int nPhoneNumber = phoneNumberRand.nextInt( 8999 ) + 1000;
			String phoneNumber = String.format( "%d", nPhoneNumber );
			elem.phone = areaCode + "-" + prefix + "-" + phoneNumber;
			
			// }
			
			// TODO: put in a command line argument to toggle this
			// if( generateEmails )
			// {
			elem.email = generateRandomString() + "@example.com";
			
			// }
			
			names.add( elem );
			System.out.println( elem );
		}
		
		
		System.out.println( "done" );		
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
	        // System.out.println( elem );
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
