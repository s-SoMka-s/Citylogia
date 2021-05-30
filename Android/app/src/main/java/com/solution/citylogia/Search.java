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
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.PlaceType;
import com.solution.citylogia.models.ShortPlace;
import com.solution.citylogia.utils.Generator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Search extends AppCompatActivity {
    ListView listView;
    SearchView searchView;
    ArrayList<PlaceType> selectedTypes;
    MyAdapter adapter;
    Double userLatitude;
    Double userLongitude;
    ArrayList<Place> allPlaces;
    ArrayList<Place> selectedPlaces;
    ArrayList<String> names;
    ArrayList<String> types;
    ArrayList<PlaceType> allTypes;
    ArrayList<Double> distances;
    ArrayList<String> distancesString;
    ArrayList<Double> marks;
    ArrayList<String> marksString;
    String selectedTypesString = "";
    Spinner filter;
    TextView textView;
    ArrayList<Long> markers;
    Long[] mapOriginal;
    Long[] map;

    enum Sort {byMarks, byDistances}

    Sort sort;
    //int images[] = {R.drawable.placeicon,R.drawable.placeicon};

    class Item {
        double distance;
        String type;
        String name;
        double mark;
        long id;

        Item(double distance, String type, String name, double mark, long id) {
            this.distance = distance;
            this.type = type;
            this.name = name;
            this.mark = mark;
            this.id = id - 13;
        }
    }

    ArrayList<Item> items;
    LatLng position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //getSupportActionBar().hide();
        userLatitude = getIntent().getDoubleExtra("user latitude", 0.0);
        userLongitude = getIntent().getDoubleExtra("user longitude", 0.0);
        Gson gson = new Gson();

        String args = getIntent().getStringExtra("all types");
        Type placeTypesType = new TypeToken<ArrayList<PlaceType>>() {
        }.getType();
        allTypes = gson.fromJson(args, placeTypesType);

        args = getIntent().getStringExtra("selected types");
        placeTypesType = new TypeToken<ArrayList<PlaceType>>() {
        }.getType();
        selectedTypes = gson.fromJson(args, placeTypesType);

        args = getIntent().getStringExtra("all places");
        placeTypesType = new TypeToken<ArrayList<Place>>() {
        }.getType();
        allPlaces = gson.fromJson(args, placeTypesType);

        args = getIntent().getStringExtra("markers");
        placeTypesType = new TypeToken<ArrayList<Long>>() {
        }.getType();
        markers = gson.fromJson(args, placeTypesType);

        position = new LatLng(userLatitude, userLongitude);

        listView = findViewById(R.id.search_list_view);
        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.selected_types);

        selectedPlaces = new ArrayList<Place>();
        for (int i = 0; i < allPlaces.size(); i++) {
            for (int j = 0; j < selectedTypes.size(); j++) {
                if (allPlaces.get(i).getId() - 13 == selectedTypes.get(j).getId() - 7) {
                    selectedPlaces.add(allPlaces.get(i));
                }
            }
        }

        mapOriginal = new Long[selectedPlaces.size()];
        for (int i = 0; i < selectedPlaces.size(); i++) {
            mapOriginal[i] = 0L;
        }
        map = mapOriginal.clone();

        for (int i = 0; i < selectedTypes.size() - 1; i++) {
            selectedTypesString += selectedTypes.get(i).getName() + ", ";
        }
        selectedTypesString += selectedTypes.get(selectedTypes.size() - 1).getName();
        textView.setText(selectedTypesString);

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
        for (int i = 0; i < selectedPlaces.size(); i++) {
            res.add(new Item(getDistance(i), selectedPlaces.get(i).getType().getName(),
                    selectedPlaces.get(i).getName(), selectedPlaces.get(i).getMark(),
                    selectedPlaces.get(i).getId()));
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
            res.add(build.append((round(items.get(i).distance, 1))).append(" км").toString());
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
            res.add(build.append("Оценка: ").append((round(items.get(i).mark, 1))).toString());
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
        ArrayList<String> rDescription3;
        ArrayList<String> rOriginalDescription1;
        ArrayList<String> rOriginalDescription2;
        ArrayList<String> rOriginalDescription3;
        int[] rImgs;

        MyAdapter(Context c, ArrayList<String> title, ArrayList<String> description1,
                  ArrayList<String> description2, ArrayList<String> description3) {
            super(c, R.layout.search_row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rOriginalTitle = new ArrayList<>(rTitle);
            this.rOriginalDescription1 = description1;
            this.rOriginalDescription2 = description2;
            this.rOriginalDescription3 = description3;
            this.rDescription1 = new ArrayList<>(rOriginalDescription1);
            this.rDescription2 = new ArrayList<>(rOriginalDescription2);
            this.rDescription3 = new ArrayList<>(rOriginalDescription3);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.search_row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription1 = row.findViewById(R.id.textView2);
            TextView myDescription2 = row.findViewById(R.id.textView3);
            TextView myDescription3 = row.findViewById(R.id.textView4);

            //images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle.get(position));
            myDescription1.setText(rDescription1.get(position));
            myDescription2.setText(rDescription2.get(position));
            myDescription3.setText(rDescription3.get(position));

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
                    //rTitle = (ArrayList<String>) results.values;
                    //notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<String> filteredResults = new ArrayList<>();

                    if (constraint == null || constraint.length() == 0) {
                        filteredResults.addAll(rOriginalTitle);
                        rDescription1 = rOriginalDescription1;
                        rDescription2 = rOriginalDescription2;
                        rDescription3 = rOriginalDescription3;
                        map = mapOriginal;
                    } else {
                        filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                    }

                    FilterResults results = new FilterResults();

                    results.values = filteredResults;

                    return results;
                }

                //@Override
                //protected FilterResults performFiltering(CharSequence constraint) {
                //  List<String> filteredResults = null;
                //if (constraint.length() == 0) {
                //  filteredResults = rOriginalTitle;
                // } else {
                //   filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                // }

                //FilterResults results = new FilterResults();
                //results.values = filteredResults;

                //return results;
                //}
            };
        }

        protected List<String> getFilteredResults(String constraint) {
            List<String> results = new ArrayList<>();

            int cnt = 0;

            for (int i = 0; i < rOriginalTitle.size(); i++) {
                if (rOriginalTitle.get(i).toLowerCase().contains(constraint)) {
                    results.add(rOriginalTitle.get(i));
                    rDescription1.set(cnt, rOriginalDescription1.get(i));
                    rDescription2.set(cnt, rOriginalDescription2.get(i));
                    rDescription3.set(cnt, rOriginalDescription3.get(i));
                    map[cnt] = mapOriginal[i];
                    cnt++;
                }
            }
            /*for (String item : rOriginalTitle) {
                if (item.toLowerCase().contains(constraint)) {
                    results.add(item);
                }
            }*/
            return results;
        }
    }

    private float distanceBetweenTwoMarkers(int i) {
        float[] results = new float[1];

        Location.distanceBetween(position.latitude, position.longitude,
                selectedPlaces.get(i).getLatitude(), selectedPlaces.get(i).getLongitude(), results);

        return results[0];
    }

    private void reBuild() {
        items = sortItems(items);
        for (int i = 0; i < selectedPlaces.size(); i++) {
            mapOriginal[i] = items.get(i).id;
        }
        map = mapOriginal.clone();
        names = getNames(items);
        distances = getDistances(items);
        distancesString = getDistancesString(items);
        types = getTypes(items);
        marks = getMarks(items);
        marksString = getMarksString(items);
        items = sortItems(items);
        adapter = new MyAdapter(this, names, types, distancesString, marksString);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent i = new Intent();
                //Intent i = new Intent(Search.this, MapActivity.class);
                //i.putExtra("selected place in search", 20);
                //setResult(RESULT_OK, i);
                //finish();
                /*String idStr = Long.toString(map[(int) id]);
                Intent intent = new Intent();
                intent.putExtra("result", idStr);
                setResult(RESULT_OK, intent);
                finish();*/
                //val placeId = markers!![result.toInt()].snippet.toLong();
                Long placeId = markers.get(Math.toIntExact(map[(int) id]));
                Intent i = new Intent(Search.this, PlaceInside.class);
                i.putExtra("id", placeId);
                startActivity(i);
                //Intent i = new Intent(Search.this, PlaceInside.class);

                //i.putExtra("id", 20); // контекст - вся инфа о месте - изу структуру!

                //startActivity(i);
                //Toast.makeText(getApplicationContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
