package com.sanca.perhitunganlistrik;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText edt_TanggalMulaiSBL;
    private EditText edt_TanggalSelesaiSBL;
    private EditText edt_PemakaianRataRata;
    private EditText edt_SisaPulsa;
    private Button btn_Hitung;
    private TextView txtHasilTagihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_TanggalMulaiSBL = findViewById(R.id.editTextTanggalMulaiSBL);
        edt_TanggalSelesaiSBL = findViewById(R.id.editTextTanggalSelesaiSBL);
        edt_PemakaianRataRata = findViewById(R.id.editTextPemakaianRataRata);
        edt_SisaPulsa = findViewById(R.id.editTextSisaPulsa);
        btn_Hitung = findViewById(R.id.buttonHitung);
        txtHasilTagihan = findViewById(R.id.textViewTotalTagihan);

        edt_TanggalMulaiSBL.setOnClickListener(v -> showDatePickerDialog(edt_TanggalMulaiSBL));
        edt_TanggalSelesaiSBL.setOnClickListener(v -> showDatePickerDialog(edt_TanggalSelesaiSBL));

        btn_Hitung.setOnClickListener(v -> {
            String strTanggalMulaiSBL = edt_TanggalMulaiSBL.getText().toString(); // inputan user : 01/12/12
            String strTanggalSelesaiSBL = edt_TanggalSelesaiSBL.getText().toString(); // inputan user : 09/12/12
            String strPemakaianRataRata = edt_PemakaianRataRata.getText().toString(); //inputan user :  370
            String strSisaPulsa = edt_SisaPulsa.getText().toString(); //inputan user :  33.20

            if (strTanggalMulaiSBL.isEmpty() || strTanggalSelesaiSBL.isEmpty() || strPemakaianRataRata.isEmpty() || strSisaPulsa.isEmpty()) {
                Toast.makeText(MainActivity.this, "Semua input harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            double PemakaianRataRata = Double.parseDouble(strPemakaianRataRata);
            double SisaPulsa = Double.parseDouble(strSisaPulsa);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date TanggalMulaiSBL = null;
            Date TanggalSelesaiSBL = null;
            try {
                TanggalMulaiSBL = format.parse(strTanggalMulaiSBL);
                TanggalSelesaiSBL = format.parse(strTanggalSelesaiSBL);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long selisihMS = Math.abs(TanggalSelesaiSBL.getTime() - TanggalMulaiSBL.getTime());
            long selisihHari = selisihMS / (24 * 60 * 60 * 1000); // hasil yang diharapkan :  9 hari




// Menghitung pemakaian per hari dan membulatkan ke bawah
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            double pemakaianPerHari = Double.parseDouble(decimalFormat.format(PemakaianRataRata / 30));

// Menghitung total SBL
            double totalSBL = pemakaianPerHari * selisihHari;

// Hitung total tagihan
            double totalTagihan = SisaPulsa - totalSBL;







            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            String totalTagihanRupiah = formatRupiah.format(totalTagihan);


            Log.d("COBA", "Selisih dalam Hari: " + selisihHari);
            Log.d("COBA", "Pemakaian rata rata : " + PemakaianRataRata);
            Log.d("COBA", "Pemakaian rata rata / hari : " + pemakaianPerHari);
            Log.d("COBA", "Sisa Pulsa : " + SisaPulsa);
            Log.d("COBA", "Total SBL: " + totalSBL);
            Log.d("COBA", "Sisa Pulsa - (Permakaian rata rata / Hari) : " + totalTagihan);

            txtHasilTagihan.setText("Total Tagihan Susulan: " + totalTagihan);

        });
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    editText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
