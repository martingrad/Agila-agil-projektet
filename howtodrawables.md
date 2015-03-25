# How to add drawables as buttons or images in layout

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#ffffff"
        android:padding="10sp"
        android:adjustViewBounds="true"
        android:scaleType="fitCenter"
        android:id="@+id/imageView1"
        android:src="@drawable/skruv_nedskalad"
        android:layout_below="@+id/questionId"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true" />

    <ImageButton
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#ffffff"
        android:padding="10sp"
        android:adjustViewBounds="true"
        android:scaleType="fitCenter"
        android:id="@+id/imageButton2"
        android:src="@drawable/skruv_nedskalad"
        android:layout_below="@+id/factTextView"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true" />


### Drawables of size 1500x297 px works fine, larger than that does not work on tablets.


