package com.example.graduationprojectgallery.helperClasses;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.graduationprojectgallery.models.Album;
import com.example.graduationprojectgallery.models.PhotoModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.net.Uri.fromFile;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.graduationprojectgallery.activities.MainActivity.urls;


public class HelperClass extends Fragment {

    //region tazzy albums data
    public static ArrayList<Album> mAlbums = new ArrayList<>();
    public static String[] albums_names_array;
    public static int albums_count = 0;
    public static ArrayList<Uri> album_thumbnails_uri = new ArrayList<>();
    public static ArrayList<Uri> album_images_uri = new ArrayList<>();


    //endregion
    public static List<PhotoModel> getPhotos(Context context) {
        // The list of columns we're interested in:
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.TITLE, MediaStore.Images.Media.SIZE};

        final Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // Specify the provider
                        columns, // The columns we're interested in
                        null, // A WHERE-filter query
                        null, // The arguments for the filter-query
                        MediaStore.Images.Media.DATE_ADDED + " DESC" // Order the results, newest first
                );


        List<PhotoModel> result = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {

            final int imagePath = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            final int date = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            final int title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            final int size = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            do {

                result.add(new PhotoModel(cursor.getString(imagePath), cursor.getString(date), cursor.getString(title), cursor.getString(size)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

/*
//    public static PhotoModel getPhotos(Context context, ArrayList<Uri> mUri, int position) {
//        // The list of columns we're interested in:
//
//        String projection[] = {
//                MediaStore.Images.Media.DATA
//                , MediaStore.Images.Media.DATE_ADDED
//                , MediaStore.Images.Media.TITLE
//                , MediaStore.Images.Media.SIZE
//        };
//
//        Uri uri = Uri.parse("content://storage/emulated/0/Download/images");
//        final Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
////                .query(mUri.get(position) , new String[]{MediaStore.Images.Media.DATA} , null, null, null);
//        cursor.moveToFirst();
//        if (cursor == null) {
//
//            cursor.close();
//        }
//
//        List<PhotoModel> result = new ArrayList<>(cursor.getCount());
//
//        if (cursor.moveToFirst()) {
//
//            final int imagePath = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            final int date = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
//            final int title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
//            final int size = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
//            do {
//
//                result.add(new PhotoModel(cursor.getString(imagePath), cursor.getString(date), cursor.getString(title), cursor.getString(size)));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return result.get(position);
//    }

*/

    public static List<String> getImagePaths(Context context) {
        // The list of columns we're interested in:
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};

        final Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // Specify the provider
                        columns, // The columns we're interested in
                        null, // A WHERE-filter query
                        null, // The arguments for the filter-query
                        MediaStore.Images.Media.DATE_ADDED + " DESC" // Order the results, newest first
                );


        List<String> result = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                result.add(cursor.getString(image_path_col));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    // TODO: 3/3/2020 implement delete photo
    public static void DeletePhoto(PhotoModel path, Context context) {
      /*  String[] columns = {MediaStore.Images.Media.DATA};
        final int cursor = context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, path.getTitle(), columns);
        if (cursor == -1) {

        }
        Toast.makeText(context, "-1", Toast.LENGTH_LONG).show();*/

    }


    public static void show(final String photo, Context context, ImageView container) {
        Glide
                .with(context)
                .load(photo)
                .override(300, 300)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(container);
    }

    public static void show(final PhotoModel photo, Context context, ImageView container) {
        Glide
                .with(context)
                .load(photo.getPath())
                .override(300, 300)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(container);

    }

    public static String ConvertTimeStampToDate(Long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }

    public static String getImageSize(long bytes) {
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1024L ? bytes + " B"
                : b <= 0xfffccccccccccccL >> 40 ? String.format("%.1f KB", bytes / 0x1p10)
                : b <= 0xfffccccccccccccL >> 30 ? String.format("%.1f MB", bytes / 0x1p20)
                : b <= 0xfffccccccccccccL >> 20 ? String.format("%.1f GB", bytes / 0x1p30)
                : b <= 0xfffccccccccccccL >> 10 ? String.format("%.1f TB", bytes / 0x1p40)
                : b <= 0xfffccccccccccccL ? String.format("%.1f PB", (bytes >> 10) / 0x1p40)
                : String.format("%.1f EB", (bytes >> 20) / 0x1p40);
    }


    //region tazzy albums methods


    public static File createAppDirectory(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission is NOT granted");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    100); // this 100 here is MY_PERMISSIONS_REQUEST_READ_CONTACTS

        } else {
            Log.v(TAG, "Permission is granted");
        }
        //region creating app directory
        String directory_path = Environment.getExternalStorageDirectory().toString();
        String directory_name = "bitirmeGaleri";
        File appDir = new File(directory_path, directory_name);
        try {
            if (appDir.exists()) {
                Log.v(TAG, "createAppDirectory : app directory already exists!");

            }

            if (!appDir.exists()) {

                appDir.mkdir();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(TAG, "app directory created!");
        //endregion

        //region creating albums directory
        directory_name = "Albums";
        File albumsDir = new File(appDir, directory_name);
        try {
            if (albumsDir.exists()) {
                Log.v(TAG, "album directory already exists!");
            }

            if (!albumsDir.exists()) {

                albumsDir.mkdir();
                Log.v(TAG, "album directory created!");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //endregion

        //region creating the albums text file
        File f1 = new File(appDir, "Albums.txt");
        try {
            f1.createNewFile();

            if (f1.exists()) {
                Log.d(TAG, "Albums.txt file created ");
            }
            if (!f1.exists()) {
                Log.d(TAG, "Albums.txt file NOT created ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //endregion

        return appDir;
    }

    public static void createNewAlbumDirectory(String album_name, Activity activity) {

        File appDir = createAppDirectory(activity);

        //region creating/opening albums directory
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission is NOT granted");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    100); // this 100 here is MY_PERMISSIONS_REQUEST_READ_CONTACTS

        } else {
            Log.v(TAG, "Permission is granted");
        }


        String directory_name = "Albums";
        File albumsDir = new File(appDir, directory_name);
        try {
            if (albumsDir.exists()) {
                Log.v(TAG, "album directory already exists!");
            }

            if (!albumsDir.exists()) {

                albumsDir.mkdir();
                Log.v(TAG, "album directory created!");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //endregion

        //region opening albums txt file checking if album already exists
        File f1 = new File(appDir, "Albums.txt");
        String[] words = null;

        try {
            FileReader fr = new FileReader(f1);  //Creation of File Reader object
            BufferedReader br = new BufferedReader(fr); //Creation of BufferedReader object
            String s;
            int count = 0;
            while ((s = br.readLine()) != null)   //Reading Content from the file
            {
                words = s.split("\\$");  //Split the word using space
                for (String word : words) {
                    if (word.equals(album_name))   //Search for the given word
                    {
                        count++;    //If Present increase the count by one
                        Log.d(TAG, "createNewAlbumDirectory: album already exists " + count);
                        Toast.makeText(activity, album_name + "Already Exists!", Toast.LENGTH_LONG).show();
                        fr.close();
                        br.close();
                        return;
                    }
                }
            }
            fr.close();
            br.close();
            Log.d(TAG, "createNewAlbumDirectory: " + album_name + " doesn't exist YET ");

        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion

        //region adding the album to albums list

        FileOutputStream fout;
        try {
            fout = new FileOutputStream(new File(appDir, "Albums.txt"), true);
            OutputStreamWriter writer = new OutputStreamWriter(fout);
            writer.write(album_name + "$");
            writer.flush();
            writer.close();
            Toast.makeText(activity, album_name + "Done!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion

        //region creating the album text file
        File f2 = new File(albumsDir, album_name + ".txt");
        try {
            f2.createNewFile();

            if (f2.exists()) {
                Log.d(TAG, album_name + "txt file created ");
            }
            if (!f2.exists()) {
                Log.d(TAG, album_name + "txt file NOT created ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //endregion

        //region create favorites album txt file

        File f3 = new File(albumsDir, "Favorites.txt");
        try {
            f3.createNewFile();

            if (f3.exists()) {
                Log.d(TAG, "Favorites.txt file created ");
            }
            if (!f3.exists()) {
                Log.d(TAG, "Favorites.txt file NOT created ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion

        //Tazzy PLEASE NOTE THAT ALBUM EXISTENCE NEEDS TO BE CHECKED BEFORE CALLING THIS
        loadAlbums(activity);
    }

    public static void loadAlbums(Activity activity) {
        Log.d(TAG, "loadAlbums: albums names are ");
        File appDirectory = createAppDirectory(activity);

        File f1 = new File(appDirectory, "Albums.txt");
        String[] words = null;
        mAlbums.removeAll(mAlbums);
        try {
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);

            Uri uri;
            String s;
            int count = 0;

            albums_names_array = new String[2];

            albums_count = 2;
            while ((s = br.readLine()) != null)   //Reading Content from the file
            {
                words = s.split("\\$");  //Split the word using space

                albums_names_array = new String[2 + words.length];
                for (String word : words) {

                    albums_names_array[count + 2] = words[count];
                    count++;
                    //  albums_names_array = Arrays.copyOf(words, words.length);
                }
                albums_count = words.length;
            }
            albums_names_array[0] = "Recent";
            albums_names_array[1] = "Favorites";


            fr.close();
            br.close();

            for (int i = 0; i < albums_names_array.length; i++) {

                mAlbums.add(new Album(albums_names_array[i], getAlbumImages(albums_names_array[i], activity)));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static Uri getAlbumImages(String album_name, Activity activity) {

        Uri uri;
        album_images_uri.removeAll(album_images_uri);
        File appDirectory = createAppDirectory(activity);

        //region checking/creating the album text file
        File albumsDir = new File(appDirectory, "Albums");
        File f2 = new File(albumsDir, album_name + ".txt");

        try {
            f2.createNewFile();

            if (f2.exists()) {
                Log.d(TAG, album_name + "txt file created ");
            }
            if (!f2.exists()) {
                Log.d(TAG, album_name + "txt file NOT created ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion

        //region empty album icon
        File f1 = new File(albumsDir, "/Users/tugbacanoglu/Desktop/myGraduationMarch/app/src/main/res/mipmap-xhdpi/empty_album_foreground.png");
        try {
            f2.createNewFile();

            if (f2.exists()) {
                Log.d(TAG, album_name + "txt file created ");
            }
            if (!f2.exists()) {
                Log.d(TAG, album_name + "txt file NOT created ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion


        uri = fromFile(f1);


        //region reading the album name txt file for images

        String s;
        int count = 0;


        try {
            FileReader fr = new FileReader(f2);
            BufferedReader br = new BufferedReader(fr);
            String[] words = null;

            while ((s = br.readLine()) != null)   //Reading Content from the file
            {
                words = s.split("\\$");  //Split the word using space


                for (String word : words) {


                    album_images_uri.add(Uri.parse(word));
                    count++;
                    //  albums_names_array = Arrays.copyOf(words, words.length);
                }
                //uri_count = words.length;
                uri = album_images_uri.get(album_images_uri.size() - 1);
            }
            album_thumbnails_uri.add(uri);
            if (album_name == "Recent") {

                uri = fromFile(new File(urls.get(1)));
            }

            fr.close();
            br.close();

            System.out.println(uri);

        } catch (Exception e) {
            e.printStackTrace();

        }

        //endregion

        return uri;

    }

    public static void addImageToAlbum(ArrayList<Uri> album_uris, String album_name, Activity activity) {

        File appDirectory = createAppDirectory(activity);

        //region checking/creating the album text file
        File albumsDir = new File(appDirectory, "Albums");
        File f2 = new File(albumsDir, album_name + ".txt");
        try {
            f2.createNewFile();

            if (f2.exists()) {
                Log.d(TAG, album_name + "txt file created ");
            }
            if (!f2.exists()) {
                Log.d(TAG, album_name + "txt file NOT created ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //endregion

        //region writing image to file

        FileOutputStream fout;
        try {
            fout = new FileOutputStream(f2, true);
            OutputStreamWriter writer = new OutputStreamWriter(fout);

            for (int i = 0; i < album_uris.size(); i++) {
                writer.write(album_uris.get(i) + "$");
            }

            writer.flush();
            writer.close();
            Toast.makeText(activity, album_name + "Done!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        album_uris.removeAll(album_uris);

        //endregion

    }


    public static Uri pathToUri(String path) {
        return fromFile(new File(path));
    }

    public static void deleteAlbum(String album_name, Activity activity) {

        File appDir = createAppDirectory(activity);

        //region creating/opening albums directory
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission is NOT granted");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    100); // this 100 here is MY_PERMISSIONS_REQUEST_READ_CONTACTS

        } else {
            Log.v(TAG, "Permission is granted");
        }


        String directory_name = "Albums";
        File albumsDir = new File(appDir, directory_name);
        try {
            if (albumsDir.exists()) {
                Log.v(TAG, "album directory already exists!");
            }

            if (!albumsDir.exists()) {

                albumsDir.mkdir();
                Log.v(TAG, "album directory created!");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //endregion

        //region opening albums txt file checking if album already exists
        File f1 = new File(appDir, "Albums.txt");
        String[] words = null;

        try {
            FileReader fr = new FileReader(f1);  //Creation of File Reader object
            BufferedReader br = new BufferedReader(fr); //Creation of BufferedReader object
            String s;
            int count = 0;
            while ((s = br.readLine()) != null)   //Reading Content from the file
            {
                words = s.split("\\$");  //Split the word using space
                for (String word : words) {
                    if (word.equals(album_name))   //Search for the given word
                    {
                        count++;    //If Present increase the count by one
                        Log.d(TAG, "createNewAlbumDirectory: album exists " + count);
                        fr.close();
                        br.close();
                    }
                }
            }
            fr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion

        //region deleting album name from albums.txt

        File tempFile = new File(appDir, "Albums.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f1));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = album_name + "$";
            String currentLine;

            while ((currentLine = reader.readLine()) != null)   //Reading Content from the file
            {
                words = currentLine.split("\\$");  //Split the word using space
                for (String word : words) {
                    if (word.equals(lineToRemove))   //Search for the given word
                    {
                        continue;
                    }
                    writer.write(word + "$");
                }
                writer.close();
                reader.close();
            }
            boolean successful = tempFile.renameTo(f1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion

        //region remove album txt file from albums directory
        File f2 = new File(albumsDir, album_name + ".txt");
        boolean deleted = f2.delete();

        //endregion
    }

    //endregion

}
