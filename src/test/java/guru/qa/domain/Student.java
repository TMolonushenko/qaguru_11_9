package guru.qa.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Student {

    //   {
    //       "name": "Tatyana",
    //           "surname": "Molonushenko",
    //           "favorit_misic": [
    //       "Metalllica",
    //               "Nirvana"
    // ],
    //       "address": {
//        "street": "Oktyabrskaya",
    //               "house": "38"
    //   }
    //   }
    public String name;
    public String surname;
    @SerializedName("favorit_music")
    public List<String> favorit_music;
    public Address address;

}
