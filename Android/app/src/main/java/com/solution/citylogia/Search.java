package com.solution.citylogia;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.ShortPlace;
import com.solution.citylogia.utils.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Search extends AppCompatActivity {
    ListView listView;
    SearchView searchView;
    ArrayList<String> stringArrayList = new ArrayList<>();
    MyAdapter adapter;
    ArrayList<Place> places;
    ArrayList<String> names;
    ArrayList<String> types;
    ArrayList<Double> distances;
    ArrayList<String> distancesString;
    ArrayList<Double> marks;
    ArrayList<String> marksString;
    String selectedTypesString;
    String[] typeArray = {"Парки", "Архитектура", "Еда", "Другое"};
    Spinner filter;
    TextView textView;
    ArrayList<Boolean> selectedTypes;
    static final int AMOUNT_OF_TYPES = 4;

    enum Sort {byMarks, byDistances}

    ;
    Sort sort;
    //int images[] = {R.drawable.placeicon,R.drawable.placeicon};

    class Item {
        double distance;
        String type;
        String name;
        double mark;

        Item(double distance, String type, String name, double mark) {
            this.distance = distance;
            this.type = type;
            this.name = name;
            this.mark = mark;
        }
    }

    ArrayList<Item> items;
    LatLng position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Generator generator = new Generator();
        this.places = generator.genPlaces(10);
        setContentView(R.layout.activity_search);
        //getSupportActionBar().hide();
        position = getIntent().getParcelableExtra("user position");
        //selectedTypes = getIntent().getBooleanArrayExtra("selected types");
        listView = findViewById(R.id.search_list_view);
        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.selected_types);

        textView.setText(getSelectedTypesString());

        items = getItems();
        reBuild();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Filter a;
                a = adapter.getFilter();
                a.filter(newText);
                return false;
            }
        });

        filter = findViewById(R.id.spinner_filter_2);
        String[] value = {"По расстоянию", "По рейтингу"};
        ArrayList<String> valueList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_spinner, valueList);
        filter.setAdapter(arrayAdapter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sort = Sort.byDistances;
                } else {
                    sort = Sort.byMarks;
                }
                reBuild();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sort = Sort.byDistances;
                reBuild();
            }
        });

    }

    private ArrayList<Item> getItems() {
        ArrayList<Item> res = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
                res.add(new Item(getDistance(i), places.get(i).getType().getName(), places.get(i).getName(), places.get(i).getMark()));
        }
        return res;
    }

    private ArrayList<Item> sortItems(ArrayList<Item> res) {
        if (sort == Sort.byDistances) {
            Collections.sort(res, new SortByDistance());
        } else if (sort == Sort.byMarks) {
            Collections.sort(res, new SortByMark());
            Collections.reverse(res);
        }
        return res;
    }

    private ArrayList<String> getNames(ArrayList<Item> items) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            res.add(items.get(i).name);
        }
        return res;
    }

    private ArrayList<Double> getDistances(ArrayList<Item> items) {
        ArrayList<Double> res = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            res.add(items.get(i).distance);
        }
        return res;
    }

    private ArrayList<String> getDistancesString(ArrayList<Item> items) {
        ArrayList<String> res = new ArrayList<>();
        StringBuilder build;
        for (int i = 0; i < items.size(); i++) {
            build = new StringBuilder();
            res.add(build.append(Double.toString(round(items.get(i).distance, 1))).append(" км").toString());
        }
        return res;
    }

    private double getDistance(int i) {
        return (double) (distanceBetweenTwoMarkers(i) / 1000);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public ArrayList<String> getTypes(ArrayList<Item> items) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            res.add(items.get(i).type);
        }
        return res;
    }

    public ArrayList<Double> getMarks(ArrayList<Item> items) {
        ArrayList<Double> res = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            res.add(items.get(i).mark);
        }
        return res;
    }

    private ArrayList<String> getMarksString(ArrayList<Item> items) {
        ArrayList<String> res = new ArrayList<>();
        StringBuilder build;
        for (int i = 0; i < items.size(); i++) {
            build = new StringBuilder();
            res.add(build.append("Оценка: ").append(Double.toString(round(items.get(i).mark, 1))).toString());
        }
        return res;
    }

    class SortByDistance implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return (int) ((o1.distance - o2.distance) * 1000000);
        }
    }

    class SortByMark implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return (int) ((o1.mark - o2.mark) * 1000000);
        }
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> rTitle;
        ArrayList<String> rOriginalTitle;
        ArrayList<String> rDescription1;
        ArrayList<String> rDescription2;
        int[] rImgs;

        MyAdapter(Context c, ArrayList<String> title, ArrayList<String> description1, ArrayList<String> description2) {
            super(c, R.layout.search_row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rOriginalTitle = new ArrayList<>(rTitle);
            this.rDescription1 = description1;
            this.rDescription2 = description2;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.search_row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription1 = row.findViewById(R.id.textView2);
            TextView myDescription2 = row.findViewById(R.id.textView3);

            //images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle.get(position));
            myDescription1.setText(rDescription1.get(position));
            myDescription2.setText(rDescription2.get(position));

            return row;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    rTitle.clear();
                    rTitle.addAll((List) results.values);
                    notifyDataSetChanged();
                    /*rTitle = (ArrayList<String>) results.values;
                    notifyDataSetChanged();*/
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<String> filteredResults = new ArrayList<>();

                    if (constraint == null || constraint.length() == 0) {
                        filteredResults.addAll(rOriginalTitle);
                    } else {
                        filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                    }

                    FilterResults results = new FilterResults();
                    results.values = filteredResults;

                    return results;
                }

                /*@Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<String> filteredResults = null;
                    if (constraint.length() == 0) {
                        filteredResults = rOriginalTitle;
                    } else {
                        filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                    }

                    FilterResults results = new FilterResults();
                    results.values = filteredResults;

                    return results;
                }*/
            };
        }

        protected List<String> getFilteredResults(String constraint) {
            List<String> results = new ArrayList<>();

            for (String item : rOriginalTitle) {
                if (item.toLowerCase().contains(constraint)) {
                    results.add(item);
                }
            }
            return results;
        }
    }

    private float distanceBetweenTwoMarkers(int i) {
        float[] results = new float[1];

        Location.distanceBetween(position.latitude, position.longitude,
                places.get(i).getLatitude(), places.get(i).getLongitude(), results);

        return results[0];
    }

    private void reBuild() {
        items = sortItems(items);
        names = getNames(items);
        distances = getDistances(items);
        distancesString = getDistancesString(items);
        types = getTypes(items);
        marks = getMarks(items);
        marksString = getMarksString(items);
        items = sortItems(items);
        adapter = new MyAdapter(this, names, types, distancesString);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra("selected places in search", position);
                setResult(RESULT_OK, i);
                finish();
                /*Intent i = new Intent(Search.this, PlaceInside.class);

                i.putExtra("selected places in search", position); // контекст - вся инфа о месте - изу структуру!

                startActivity(i);*/
                //Toast.makeText(getApplicationContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSelectedTypesString() {
        StringBuilder res = new StringBuilder();
        boolean flag = false;

        //this.selectedTypes


        return res.toString();
    }

}
