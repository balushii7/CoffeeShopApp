package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private TextView textViewItemName, textViewItemPrice, textViewPaymentMessage;
    private EditText editTextQuantity, editTextCardNumber, editTextCVV, editTextExpiryDate;
    private RadioGroup radioGroupPaymentMethod;
    private LinearLayout linearLayoutCardDetails;
    private Button buttonSubmitPayment, buttonBackHome;
    private FirebaseFirestore db;
    private String itemId, itemName;
    private double itemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        db = FirebaseFirestore.getInstance();

        textViewItemName = findViewById(R.id.textViewItemName);
        textViewItemPrice = findViewById(R.id.textViewItemPrice);
        textViewPaymentMessage = findViewById(R.id.textViewPaymentMessage);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextCVV = findViewById(R.id.editTextCVV);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod);
        linearLayoutCardDetails = findViewById(R.id.linearLayoutCardDetails);
        buttonSubmitPayment = findViewById(R.id.buttonSubmitPayment);
        buttonBackHome = findViewById(R.id.buttonBackHome);

        itemId = getIntent().getStringExtra("itemId");
        itemName = getIntent().getStringExtra("itemName");
        itemPrice = getIntent().getDoubleExtra("price", 0);

        textViewItemName.setText(itemName);
        textViewItemPrice.setText(String.format("Price: %.3f OMR", itemPrice));

        radioGroupPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonCard) {
                linearLayoutCardDetails.setVisibility(View.VISIBLE);
            } else {
                linearLayoutCardDetails.setVisibility(View.GONE);
            }
        });

        buttonSubmitPayment.setOnClickListener(v -> submitPayment());
        buttonBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitPayment() {
        String quantityStr = editTextQuantity.getText().toString();
        int quantity = Integer.parseInt(quantityStr.isEmpty() ? "0" : quantityStr);
        double totalBill = quantity * itemPrice;

        Map<String, Object> order = new HashMap<>();
        order.put("itemId", itemId);
        order.put("userEmail", getUserEmail());
        order.put("totalBill", totalBill);
        order.put("preparationStatus", "Preparing");

        if (radioGroupPaymentMethod.getCheckedRadioButtonId() == R.id.radioButtonCard) {
            String cardNumber = editTextCardNumber.getText().toString();
            String cvv = editTextCVV.getText().toString();
            String expiryDate = editTextExpiryDate.getText().toString();

            if (TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(cvv) || TextUtils.isEmpty(expiryDate)) {
                textViewPaymentMessage.setText("Please complete all fields");
                return;
            }

            order.put("cardNumber", cardNumber);
            order.put("cvv", cvv);
            order.put("expiryDate", expiryDate);
        }

        db.collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    textViewPaymentMessage.setText("Order placed successfully!");
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    textViewPaymentMessage.setText("Failed to place order. Try again.");
                });
    }

    private String getUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getEmail() : "guest@example.com";
    }

    private void clearFields() {
        editTextQuantity.setText("");
        editTextCardNumber.setText("");
        editTextCVV.setText("");
        editTextExpiryDate.setText("");
    }
}
