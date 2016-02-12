package paxtonpoole.flue0;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    private Firebase oneRef;
    private Firebase twoRef;
    private Firebase threeRef;
    private Firebase fourRef;
    private Firebase fiveRef;

    private int one;
    private int two;
    private int three;
    private int four;
    private int five;

    private double averageHealth = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        displayAlertDialog();
        infoButton();
        keyButton();
        excellentButton();
        goodButton();
        averageButton();
        poorButton();
        terribleButton();
        onStart();

        oneRef   = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/one");
        twoRef   = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/two");
        threeRef = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/three");
        fourRef  = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/four");
        fiveRef  = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/five");

        //Updates number of people with or with out the flu

        oneRef.addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(DataSnapshot dataSnapshot) {
                                             one = dataSnapshot.getValue(int.class);
                                         }

                                         @Override
                                         public void onCancelled(FirebaseError firebaseError) {

                                         }
                                     }
        );

        twoRef.addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(DataSnapshot dataSnapshot) {
                                             two = dataSnapshot.getValue(int.class);
                                         }

                                         @Override
                                         public void onCancelled(FirebaseError firebaseError) {

                                         }
                                     }

        );
        threeRef.addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               three = dataSnapshot.getValue(int.class);
                                           }

                                           @Override
                                           public void onCancelled(FirebaseError firebaseError) {

                                           }
                                       }

        );
        fourRef.addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              four = dataSnapshot.getValue(int.class);
                                          }

                                          @Override
                                          public void onCancelled(FirebaseError firebaseError) {

                                          }
                                      }

        );
        fiveRef.addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              five = dataSnapshot.getValue(int.class);
                                          }

                                          @Override
                                          public void onCancelled(FirebaseError firebaseError) {

                                          }
                                      }

        );
    }

    public void displayAlertDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.warning, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Notice");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("I'm from Camden", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alert.setPositiveButton("I'm not from Camden", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public double getAverageHealth()
    {
        if(one == 0 && two == 0 && three == 0 && four == 0 && five == 0)
        {
            averageHealth = 0.0;
            return 0.0;
        }
        else
        {
            averageHealth = ((((double) one) + (two * 2) + (three * 3) + (four * 4) + (five * 5)) / (one + two + three + four + five));
            return averageHealth;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Button oneButton    = (Button)findViewById(R.id.oneButton);
        Button twoButton    = (Button)findViewById(R.id.twoButton);
        Button threeButton  = (Button)findViewById(R.id.threeButton);
        Button fourButton   = (Button)findViewById(R.id.fourButton);
        Button fiveButton   = (Button)findViewById(R.id.fiveButton);
        Button update       = (Button)findViewById(R.id.update);

        final TextView t1 = (TextView)findViewById(R.id.influenzaPercent);

        oneRef   = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/one");
        twoRef   = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/two");
        threeRef = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/three");
        fourRef  = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/four");
        fiveRef  = new Firebase("https://mofluepoint0.firebaseio.com/numbersChosen/five");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 1, 2, 5, 0);

        Calendar currentCalendar = Calendar.getInstance();

        if(calendar.before(currentCalendar))
        {

            update.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getAverageHealth() == 0)
                            {
                                t1.setText("Wait until 2/1/2016 to report your health.");
                            }
                            else
                            {
                                t1.setText(getAverageHealth() + " is the average health rating that people have reported this week in Camden County Missouri.");

                            }
                        }
                    });

            oneButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!(read().equals("Pressed1")))
                                {
                                    one += 1;
                                    oneRef.setValue(one);

                                    t1.setText(getAverageHealth() + " is the average health rating that people have reported this week in Camden County Missouri.");
                                    save();
                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(), "You can only report in once",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            twoButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                if(!(read().equals("Pressed1")))
                                {
                                    two += 1;
                                    twoRef.setValue(two);

                                    t1.setText(getAverageHealth() + " is the average health rating that people have reported this week in Camden County Missouri.");
                                    save();
                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(), "You can only report in once",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            threeButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            if(!(read().equals("Pressed1")))
                            {
                                three += 1;
                                threeRef.setValue(three);

                                t1.setText(getAverageHealth() + " is the average health rating that people have reported this week in Camden County Missouri.");
                                save();
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "You can only report in once",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            fourButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!(read().equals("Pressed1"))) {
                                four += 1;
                                fourRef.setValue(four);

                                t1.setText(getAverageHealth() + " is the average health rating that people have reported this week in Camden County Missouri.");
                                save();
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "You can only report in once",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            fiveButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!(read().equals("Pressed1"))) {
                                five += 1;
                                fiveRef.setValue(five);

                                t1.setText(getAverageHealth() + " is the average health rating that people have reported this week in Camden County Missouri.");
                                save();
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "You can only report in once",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }

    }

    public void infoButton()
    {
        Button toSecondActivity = (Button)findViewById(R.id.infoButton);
        toSecondActivity.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        startActivity(new Intent(MainActivity.this, Information.class));
                    }
                }
        );
    }

    public void keyButton()
    {
        Button toSecondActivity = (Button)findViewById(R.id.keyButton);
        toSecondActivity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, KeyFactsAboutInfluenza.class));
                    }
                }
        );
    }

    public void excellentButton()
    {
        Button toSecondActivity = (Button)findViewById(R.id.excellentButton);
        toSecondActivity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, ExcellentReq.class));
                    }
                }
        );
    }

    public void goodButton()
    {
        Button toSecondActivity = (Button)findViewById(R.id.goodButton);
        toSecondActivity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, GoodReq.class));
                    }
                }
        );
    }

    public void averageButton()
    {
        Button toSecondActivity = (Button)findViewById(R.id.averageButton);
        toSecondActivity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AverageReq.class));
                    }
                }
        );
    }

    public void poorButton()
    {
        Button toSecondActivity = (Button)findViewById(R.id.poorButton);
        toSecondActivity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, PoorReq.class));
                    }
                }
        );
    }

    public void terribleButton()
    {
        Button toSecondActivity = (Button)findViewById(R.id.terribleButton);
        toSecondActivity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, TerribleReq.class));
                    }
                }
        );
    }

    public void save()
    {
        String filename = "flue.0";
        String string = "Pressed1";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read()
    {
        FileInputStream fis;
        final StringBuffer storedString = new StringBuffer();

        try
        {
            fis = openFileInput("flue.0");
            BufferedReader dataIO = new BufferedReader(new InputStreamReader(fis));
            String strLine = null;

            if ((strLine = dataIO.readLine()) != null)
            {
                storedString.append(strLine);
                return strLine;
            }

            dataIO.close();
            fis.close();
        }
        catch  (Exception e)
        {
            return "no";
        }

        return "no";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
