package com.example.tourguidemegaphone.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tourguidemegaphone.model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizDao {
    private QuizDbHelper dbHelper;
    //private SQLiteDatabase db;

    private static QuizDao instance;

    private QuizDao(){};

    public static QuizDao getInstance(Context context){
        if (instance == null)
            instance = new QuizDao(context);
        return instance;
    }

    private QuizDao(Context context) {
        dbHelper = new QuizDbHelper(context);
        dbHelper.emptyAllTables();
        initDb();
        //db = dbHelper.getReadableDatabase();
    }

    public List<Quiz> getQuizForCity(String city) {
        List<Quiz> quizList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                QuizContract.QuizEntry.COLUMN_QUESTION,
                QuizContract.QuizEntry.COLUMN_OPTION1,
                QuizContract.QuizEntry.COLUMN_OPTION2,
                QuizContract.QuizEntry.COLUMN_OPTION3,
                QuizContract.QuizEntry.COLUMN_OPTION4,
                QuizContract.QuizEntry.COLUMN_ANSWER
        };

        String selection = QuizContract.QuizEntry.COLUMN_CITY + " = ?";
        String[] selectionArgs = {city};

        Cursor cursor = db.query(
                QuizContract.QuizEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String question = cursor.getString(cursor.getColumnIndexOrThrow(QuizContract.QuizEntry.COLUMN_QUESTION));
            String option1 = cursor.getString(cursor.getColumnIndexOrThrow(QuizContract.QuizEntry.COLUMN_OPTION1));
            String option2 = cursor.getString(cursor.getColumnIndexOrThrow(QuizContract.QuizEntry.COLUMN_OPTION2));
            String option3 = cursor.getString(cursor.getColumnIndexOrThrow(QuizContract.QuizEntry.COLUMN_OPTION3));
            String option4 = cursor.getString(cursor.getColumnIndexOrThrow(QuizContract.QuizEntry.COLUMN_OPTION4));
            int answer = cursor.getInt(cursor.getColumnIndexOrThrow(QuizContract.QuizEntry.COLUMN_ANSWER));

            Quiz quiz = new Quiz(question, option1, option2, option3, option4, answer);
            quizList.add(quiz);
        }

        cursor.close();
        db.close();

        return quizList;
    }



    public void initDb() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Questions for Vancouver, Canada
        addQuestion(db, "Vancouver", "What is the most famous suspension bridge in Vancouver?", "Golden Gate Bridge", "Lions Gate Bridge", "Brooklyn Bridge", "Sydney Harbour Bridge", 2);
        addQuestion(db, "Vancouver", "Which popular ski resort is located near Vancouver?", "Whistler Blackcomb", "Aspen Mountain", "Jackson Hole", "Banff Sunshine Village", 1);
        addQuestion(db, "Vancouver", "What is the name of the large public park located in the heart of Vancouver?", "Central Park", "Stanley Park", "Golden Gate Park", "Hyde Park", 2);
        addQuestion(db, "Vancouver", "Which famous market in Vancouver is known for its fresh seafood, produce, and artisan goods?", "Pike Place Market", "Granville Island Public Market", "Borough Market", "La Boqueria", 2);
        addQuestion(db, "Vancouver", "What is the name of the popular suspension bridge in Vancouver that offers breathtaking views of the surrounding nature?", "Brooklyn Bridge", "Golden Gate Bridge", "Capilano Suspension Bridge", "Sydney Harbour Bridge", 3);
        addQuestion(db, "Vancouver", "Which event was held in Vancouver in 2010, bringing together athletes from around the world?", "FIFA World Cup", "Olympic Games", "Commonwealth Games", "Super Bowl", 2);

        // Questions for London, UK
        addQuestion(db, "London", "Which historical monument is located on the River Thames in London?", "Big Ben", "Eiffel Tower", "Tower Bridge", "Taj Mahal", 3);
        addQuestion(db, "London", "Which London park is famous for its Speaker's Corner, a traditional site for public speeches and debates?", "Regent's Park", "Hyde Park", "St. James's Park", "Green Park", 2);
        addQuestion(db, "London", "What is the name of the famous clock tower located at the north end of the Palace of Westminster in London?", "Big Ben", "Eiffel Tower", "Tower Bridge", "London Eye", 1);
        addQuestion(db, "London", "Which London museum houses a vast collection of art and artifacts, including the Rosetta Stone and the Elgin Marbles?", "British Museum", "Natural History Museum", "Tate Modern", "Victoria and Albert Museum", 1);
        addQuestion(db, "London", "Which iconic London landmark features a glass observation wheel and offers panoramic views of the city?", "Big Ben", "Eiffel Tower", "London Eye", "Tower Bridge", 3);
        addQuestion(db, "London", "What is the name of the famous pedestrian shopping street in the West End of London?", "Oxford Street", "Regent Street", "Bond Street", "Piccadilly", 1);

        // Questions for Egypt
        addQuestion(db, "Cairo", "What is the name of the famous ancient Egyptian pyramid complex located on the outskirts of Cairo?", "Pyramids of Giza", "Luxor Temple", "Karnak Temple", "Valley of the Kings", 1);
        addQuestion(db, "Cairo", "Which river flows through Cairo and is often referred to as the lifeline of Egypt?", "Nile River", "Amazon River", "Yangtze River", "Mississippi River", 1);
        addQuestion(db, "Cairo", "What is the name of the historic Islamic landmark in Cairo that is known for its towering minarets and intricate mosaics?", "Sultan Ahmed Mosque", "Blue Mosque", "Al-Aqsa Mosque", "Mosque of Muhammad Ali", 4);
        addQuestion(db, "Cairo", "Which museum in Cairo is home to an extensive collection of ancient Egyptian artifacts, including the treasures of Tutankhamun?", "Egyptian Museum", "Louvre Museum", "British Museum", "Metropolitan Museum of Art", 1);
        addQuestion(db, "Cairo", "What is the name of the bustling market district in Cairo known for its labyrinth of narrow streets, shops, and stalls selling a variety of goods?", "Khan El-Khalili", "Souq Waqif", "Grand Bazaar", "Spice Bazaar", 1);


        // Questions for Buenos Aires
        addQuestion(db, "Buenos Aires", "What is the famous dance of Argentina, which originated in Buenos Aires?", "Samba", "Tango", "Flamenco", "Waltz", 2);
        addQuestion(db, "Buenos Aires", "What is the name of the famous avenue in Buenos Aires known for its wide boulevards, tree-lined streets, and iconic obelisk?", "Broadway", "Champs-Élysées", "Avenida 9 de Julio", "The Magnificent Mile", 3);
        addQuestion(db, "Buenos Aires", "What is the name of the colorful neighborhood in Buenos Aires known for its vibrant street art and bohemian atmosphere?", "San Telmo", "La Boca", "Recoleta", "Palermo", 2);
        addQuestion(db, "Buenos Aires", "What is the name of the traditional Argentinean barbecue, where various meats are cooked on a grill over an open flame?", "Empanadas", "Chimichurri", "Asado", "Milanesa", 3);
        addQuestion(db, "Buenos Aires", "Which historical square in Buenos Aires is the site of the presidential palace and is famous for its weekly demonstrations?", "Plaza de Mayo", "Plaza Dorrego", "Plaza de la República", "Plaza San Martín", 1);
        addQuestion(db, "Buenos Aires", "What is the name of the famous cemetery in Buenos Aires known for its elaborate mausoleums, including the tomb of Eva Perón?", "La Recoleta Cemetery", "Père Lachaise Cemetery", "Arlington National Cemetery", "Mount Auburn Cemetery", 1);

        // Questions for Bogota
        addQuestion(db, "Bogota", "What is the main ingredient in Colombia's national dish 'Bandeja Paisa'?", "Fish", "Chicken", "Beef", "Pork", 3);
        addQuestion(db, "Bogota", "What is the name of the famous square in Bogota that serves as a focal point for cultural events and political protests?", "Plaza de Bolívar", "Plaza de las Nieves", "Plaza de San Diego", "Plaza de la Libertad", 1);
        addQuestion(db, "Bogota", "Which hill in Bogota offers panoramic views of the city and is home to the Sanctuary of Monserrate?", "Cerro de Monserrate", "Cerro de Guadalupe", "Cerro de la Popa", "Cerro de la Silla", 1);
        addQuestion(db, "Bogota", "What is the name of the famous gold museum in Bogota that showcases pre-Columbian artifacts and gold artifacts?", "Museo del Oro", "Museo Botero", "Museo Nacional de Colombia", "Museo de Arte Moderno de Bogotá", 1);
        addQuestion(db, "Bogota", "Which Colombian artist is known for his oversized sculptures depicting human figures, many of which are displayed in public spaces in Bogota?", "Fernando Botero", "Gabriel García Márquez", "Frida Kahlo", "Pablo Picasso", 1);
        addQuestion(db, "Bogota", "What is the name of the famous Colombian dish that consists of a deep-fried cornmeal patty filled with meat, potatoes, and vegetables?", "Empanadas", "Arepa", "Tamal", "Bandeja Paisa", 3);

        // Questions for Madrid
        addQuestion(db, "Madrid", "Which museum in Madrid is home to Pablo Picasso's famous painting 'Guernica'?", "Louvre Museum", "Museo del Prado", "British Museum", "The Metropolitan Museum of Art", 2);
        addQuestion(db, "Madrid", "What is the name of the famous art museum in Madrid that houses one of the finest collections of European art, including works by Velázquez, Goya, and El Greco?", "Prado Museum", "Louvre Museum", "Rijksmuseum", "Uffizi Gallery", 1);
        addQuestion(db, "Madrid", "Which historic square in Madrid is known for its lively atmosphere, street performers, and numerous cafes and restaurants?", "Plaza de España", "Puerta del Sol", "Plaza Mayor", "Parque del Retiro", 3);
        addQuestion(db, "Madrid", "What is the name of the royal palace in Madrid that is the official residence of the Spanish Royal Family?", "Alhambra", "Palace of Versailles", "Palacio Real", "Buckingham Palace", 3);
        addQuestion(db, "Madrid", "Which popular street in Madrid is famous for its vibrant nightlife, bars, and clubs?", "Gran Vía", "La Rambla", "Paseo del Prado", "Calle de la Montera", 1);
        addQuestion(db, "Madrid", "What is the name of the large public square in Madrid that is home to the statue of King Charles III and the famous Tio Pepe neon sign?", "Plaza de Cibeles", "Plaza de Colón", "Plaza de Callao", "Puerta del Sol", 2);


        // Questions for Berlin
        addQuestion(db, "Berlin", "Which historic Berlin landmark was a symbol of the division between East and West Berlin during the Cold War?", "Brandenburg Gate", "Eiffel Tower", "The Reichstag", "Checkpoint Charlie", 1);
        addQuestion(db, "Berlin", "Which historic Berlin landmark is a symbol of German reunification and is located near the former border between East and West Berlin?", "Brandenburg Gate", "Checkpoint Charlie", "Berlin Wall", "Reichstag Building", 1);
        addQuestion(db, "Berlin", "What is the name of the famous street in Berlin known for its vibrant nightlife, clubs, and bars?", "Alexanderplatz", "Friedrichstraße", "Kurfürstendamm", "Oranienstraße", 4);
        addQuestion(db, "Berlin", "Which Berlin museum is dedicated to the history of the city and is located in a former railway station?", "Pergamon Museum", "Jewish Museum Berlin", "DDR Museum", "Deutsches Historisches Museum", 3);
        addQuestion(db, "Berlin", "What is the name of the large public square in Berlin that is home to the Berlin Cathedral and the Altes Museum?", "Gendarmenmarkt", "Alexanderplatz", "Potsdamer Platz", "Museum Island", 4);        addQuestion(db, "Berlin", "Which Berlin neighborhood is known for its alternative culture, street art, and vibrant arts scene?", "Mitte", "Charlottenburg", "Kreuzberg", "Prenzlauer Berg", 3);


        // Questions for Paris
        addQuestion(db, "Paris", "Which famous Parisian cathedral was the setting for Victor Hugo's novel 'The Hunchback of Notre-Dame'?", "Saint Basil's Cathedral", "St. Peter's Basilica", "Notre-Dame Cathedral", "Westminster Abbey", 3);
        addQuestion(db, "Paris", "What is the name of the famous cathedral in Paris known for its Gothic architecture and flying buttresses?", "Notre-Dame Cathedral", "Sainte-Chapelle", "Sacre-Coeur Basilica", "Chartres Cathedral", 1);
        addQuestion(db, "Paris", "Which museum in Paris is home to the famous painting 'Mona Lisa' by Leonardo da Vinci?", "Musée d'Orsay", "Louvre Museum", "Musée Rodin", "Centre Pompidou", 2);
        addQuestion(db, "Paris", "What is the name of the famous boulevard in Paris known for its theaters, cafes, and luxury shops?", "Champs-Élysées", "Boulevard Haussmann", "Avenue Montaigne", "Rue de Rivoli", 1);
        addQuestion(db, "Paris", "Which historic square in Paris is home to the famous obelisk and is a popular gathering place for celebrations and demonstrations?", "Place de la Bastille", "Place de la République", "Place de la Concorde", "Place Vendôme", 3);
        addQuestion(db, "Paris", "What is the name of the famous art museum in Paris known for its collection of Impressionist and Post-Impressionist masterpieces?", "Musée d'Orsay", "Louvre Museum", "Musée Rodin", "Centre Pompidou", 1);

        // Questions for New York
        addQuestion(db, "New York", "Which iconic New York City skyscraper was completed in 1931 and was the tallest building in the world until 1970?", "Empire State Building", "One World Trade Center", "Chrysler Building", "Rockefeller Center", 1);
        addQuestion(db, "New York", "Which iconic New York City park is one of the most visited urban parks in the United States?", "Central Park", "Golden Gate Park", "Hyde Park", "High Line Park", 1);
        addQuestion(db, "New York", "What is the name of the famous street in New York known for its theaters, bright lights, and bustling atmosphere?", "Broadway", "Fifth Avenue", "Wall Street", "Park Avenue", 1);
        addQuestion(db, "New York", "Which New York City borough is home to the Statue of Liberty and Ellis Island?", "Manhattan", "Brooklyn", "Queens", "Staten Island", 4);
        addQuestion(db, "New York", "What is the name of the iconic skyscraper in New York City that was completed in 1931 and was the tallest building in the world until 1970?", "Empire State Building", "Chrysler Building", "One World Trade Center", "Rockefeller Center", 1);
        addQuestion(db, "New York", "What is the name of the famous performing arts venue in New York City that hosts the annual Tony Awards ceremony?", "Madison Square Garden", "Radio City Music Hall", "Lincoln Center for the Performing Arts", "Carnegie Hall", 2);

        // Questions for Islamabad, Pakistan
        addQuestion(db, "Islamabad", "What is the name of the iconic mosque in Islamabad, one of the largest mosques in the world?", "Badshahi Mosque", "Faisal Mosque", "Hagia Sophia", "Blue Mosque", 2);
        addQuestion(db, "Islamabad", "Which mountain range is visible from Islamabad and serves as a picturesque backdrop to the city?", "Himalayas", "Andes", "Rocky Mountains", "Margalla Hills", 4);
        addQuestion(db, "Islamabad", "What is the name of the official residence of the Prime Minister of Pakistan, located in Islamabad?", "White House", "10 Downing Street", "Parliament House", "Prime Minister's House", 4);
        addQuestion(db, "Islamabad", "Which lake is located on the outskirts of Islamabad and is a popular recreational spot for locals and tourists?", "Lake Tahoe", "Lake Geneva", "Lake Titicaca", "Rawal Lake", 4);
        addQuestion(db, "Islamabad", "Which monument in Islamabad is dedicated to the founders of Pakistan and offers panoramic views of the city?", "Minar-e-Pakistan", "Pakistan Monument", "Lahore Gate", "Quaid-e-Azam Residency", 2);


        db.close();
    }

    private void addQuestion(SQLiteDatabase db, String city, String question, String option1, String option2, String option3, String option4, int answer) {
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizEntry.COLUMN_CITY, city);
        values.put(QuizContract.QuizEntry.COLUMN_QUESTION, question);
        values.put(QuizContract.QuizEntry.COLUMN_OPTION1, option1);
        values.put(QuizContract.QuizEntry.COLUMN_OPTION2, option2);
        values.put(QuizContract.QuizEntry.COLUMN_OPTION3, option3);
        values.put(QuizContract.QuizEntry.COLUMN_OPTION4, option4);
        values.put(QuizContract.QuizEntry.COLUMN_ANSWER, answer);
        db.insert(QuizContract.QuizEntry.TABLE_NAME, null, values);
    }

    public List<String> getCitiesWithQuizzes() {
        List<String> cityList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {QuizContract.QuizEntry.COLUMN_CITY};

        Cursor cursor = db.query(
                QuizContract.QuizEntry.TABLE_NAME,
                projection,
                null,
                null,
                QuizContract.QuizEntry.COLUMN_CITY,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String city = cursor.getString(cursor.getColumnIndexOrThrow(QuizContract.QuizEntry.COLUMN_CITY));
            if (!cityList.contains(city)) {
                cityList.add(city);
            }
        }

        cursor.close();
        db.close();

        return cityList;
    }

}
