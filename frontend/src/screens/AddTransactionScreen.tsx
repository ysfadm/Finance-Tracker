import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
  Alert,
  ScrollView,
  Switch,
} from "react-native";
import { StackNavigationProp } from "@react-navigation/stack";
import DatePicker from "react-native-date-picker";
import axios from "axios";

type TransactionNavigationProp = StackNavigationProp<any, "AddTransaction">;

interface Props {
  navigation: TransactionNavigationProp;
}

interface CreateTransactionRequest {
  title: string;
  description: string;
  amount: number;
  type: "INCOME" | "EXPENSE";
  categoryId: number;
  transactionDate: string;
}

export const AddTransactionScreen: React.FC<Props> = ({ navigation }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");
  const [type, setType] = useState<"INCOME" | "EXPENSE">("EXPENSE");
  const [categoryId, setCategoryId] = useState("");
  const [date, setDate] = useState(new Date());
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<Record<string, string>>({});

  const validateForm = (): boolean => {
    const newErrors: typeof errors = {};

    if (!title.trim()) {
      newErrors.title = "Title is required";
    }

    if (!amount || parseFloat(amount) <= 0) {
      newErrors.amount = "Valid amount is required";
    }

    if (!categoryId) {
      newErrors.categoryId = "Category is required";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleCreateTransaction = async () => {
    if (!validateForm()) return;

    setLoading(true);
    try {
      const payload: CreateTransactionRequest = {
        title: title.trim(),
        description: description.trim(),
        amount: parseFloat(amount),
        type,
        categoryId: parseInt(categoryId),
        transactionDate: date.toISOString(),
      };

      await axios.post("/transactions", payload, {
        headers: { Authorization: `Bearer ${global.authToken}` },
      });

      Alert.alert("Success", "Transaction created successfully");
      navigation.goBack();
    } catch (error: any) {
      const message =
        error.response?.data?.message || "Failed to create transaction";
      Alert.alert("Error", message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.header}>Add Transaction</Text>

      {/* Type Toggle */}
      <View style={styles.typeToggleContainer}>
        <View style={styles.typeToggle}>
          <TouchableOpacity
            style={[
              styles.typeButton,
              type === "EXPENSE" && styles.typeButtonActive,
            ]}
            onPress={() => setType("EXPENSE")}
          >
            <Text
              style={[
                styles.typeButtonText,
                type === "EXPENSE" && styles.typeButtonTextActive,
              ]}
            >
              Expense
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[
              styles.typeButton,
              type === "INCOME" && styles.typeButtonActive,
            ]}
            onPress={() => setType("INCOME")}
          >
            <Text
              style={[
                styles.typeButtonText,
                type === "INCOME" && styles.typeButtonTextActive,
              ]}
            >
              Income
            </Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Form Fields */}
      <View style={styles.form}>
        <Text style={styles.label}>Title</Text>
        <TextInput
          style={[styles.input, errors.title && styles.inputError]}
          placeholder="Enter transaction title"
          value={title}
          onChangeText={setTitle}
          editable={!loading}
        />
        {errors.title && <Text style={styles.errorText}>{errors.title}</Text>}

        <Text style={styles.label}>Amount</Text>
        <TextInput
          style={[styles.input, errors.amount && styles.inputError]}
          placeholder="Enter amount"
          keyboardType="decimal-pad"
          value={amount}
          onChangeText={setAmount}
          editable={!loading}
        />
        {errors.amount && <Text style={styles.errorText}>{errors.amount}</Text>}

        <Text style={styles.label}>Category</Text>
        <TextInput
          style={[styles.input, errors.categoryId && styles.inputError]}
          placeholder="Enter category ID"
          keyboardType="number-pad"
          value={categoryId}
          onChangeText={setCategoryId}
          editable={!loading}
        />
        {errors.categoryId && (
          <Text style={styles.errorText}>{errors.categoryId}</Text>
        )}

        <Text style={styles.label}>Date</Text>
        <TouchableOpacity
          style={styles.dateButton}
          onPress={() => setShowDatePicker(true)}
        >
          <Text style={styles.dateButtonText}>
            {date.toLocaleDateString()} {date.toLocaleTimeString()}
          </Text>
        </TouchableOpacity>

        <Text style={styles.label}>Description (Optional)</Text>
        <TextInput
          style={[styles.input, styles.textArea]}
          placeholder="Enter description"
          multiline
          numberOfLines={4}
          value={description}
          onChangeText={setDescription}
          editable={!loading}
        />

        <TouchableOpacity
          style={[styles.button, loading && styles.buttonDisabled]}
          onPress={handleCreateTransaction}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <Text style={styles.buttonText}>Create Transaction</Text>
          )}
        </TouchableOpacity>
      </View>

      <DatePicker
        modal
        open={showDatePicker}
        date={date}
        onConfirm={(selectedDate) => {
          setDate(selectedDate);
          setShowDatePicker(false);
        }}
        onCancel={() => setShowDatePicker(false)}
      />
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
    padding: 16,
  },
  header: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 20,
    color: "#333",
  },
  typeToggleContainer: {
    marginBottom: 24,
  },
  typeToggle: {
    flexDirection: "row",
    backgroundColor: "#fff",
    borderRadius: 8,
    padding: 4,
    shadowColor: "#000",
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  typeButton: {
    flex: 1,
    paddingVertical: 12,
    paddingHorizontal: 16,
    alignItems: "center",
    borderRadius: 6,
  },
  typeButtonActive: {
    backgroundColor: "#3498db",
  },
  typeButtonText: {
    fontSize: 14,
    fontWeight: "600",
    color: "#666",
  },
  typeButtonTextActive: {
    color: "#fff",
  },
  form: {
    backgroundColor: "#fff",
    padding: 20,
    borderRadius: 12,
    shadowColor: "#000",
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 5,
  },
  label: {
    fontSize: 14,
    fontWeight: "600",
    marginBottom: 8,
    color: "#333",
    marginTop: 16,
  },
  input: {
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
  },
  inputError: {
    borderColor: "#e74c3c",
  },
  errorText: {
    color: "#e74c3c",
    fontSize: 12,
    marginTop: 4,
  },
  textArea: {
    height: 100,
    textAlignVertical: "top",
  },
  dateButton: {
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 8,
    padding: 12,
    justifyContent: "center",
  },
  dateButtonText: {
    fontSize: 16,
    color: "#333",
  },
  button: {
    backgroundColor: "#3498db",
    padding: 14,
    borderRadius: 8,
    alignItems: "center",
    marginTop: 24,
  },
  buttonDisabled: {
    opacity: 0.6,
  },
  buttonText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },
});
