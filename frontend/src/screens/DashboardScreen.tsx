import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  FlatList,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
  RefreshControl,
} from "react-native";
import { BottomTabNavigationProp } from "@react-navigation/bottom-tabs";
import axios from "axios";
import moment from "moment";
import AsyncStorage from "@react-native-async-storage/async-storage";

type DashboardNavigationProp = BottomTabNavigationProp<any, "Dashboard">;

interface Props {
  navigation: DashboardNavigationProp;
}

interface Transaction {
  id: number;
  title: string;
  description: string;
  amount: number;
  type: "INCOME" | "EXPENSE";
  transactionDate: string;
  categoryId: number;
}

export const DashboardScreen: React.FC<Props> = ({ navigation }) => {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [totalIncome, setTotalIncome] = useState(0);
  const [totalExpense, setTotalExpense] = useState(0);

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    setLoading(true);
    try {
      const token = await AsyncStorage.getItem("authToken");
      const response = await axios.get("/transactions?page=0&size=10", {
        headers: { Authorization: `Bearer ${token}` },
      });

      const txns = response.data.content || [];
      setTransactions(txns);

      // Calculate totals
      const income = txns
        .filter((t: Transaction) => t.type === "INCOME")
        .reduce((sum: number, t: Transaction) => sum + t.amount, 0);

      const expense = txns
        .filter((t: Transaction) => t.type === "EXPENSE")
        .reduce((sum: number, t: Transaction) => sum + t.amount, 0);

      setTotalIncome(income);
      setTotalExpense(expense);
    } catch (error) {
      console.error("Failed to fetch transactions:", error);
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await fetchTransactions();
    setRefreshing(false);
  };

  const renderTransaction = ({ item }: { item: Transaction }) => (
    <TouchableOpacity
      style={styles.transactionCard}
      onPress={() => navigation.navigate("TransactionDetail", { id: item.id })}
    >
      <View style={styles.transactionInfo}>
        <Text style={styles.transactionTitle}>{item.title}</Text>
        <Text style={styles.transactionDate}>
          {moment(item.transactionDate).format("MMM DD, YYYY")}
        </Text>
      </View>
      <Text
        style={[
          styles.transactionAmount,
          { color: item.type === "INCOME" ? "#27ae60" : "#e74c3c" },
        ]}
      >
        {item.type === "INCOME" ? "+" : "-"}${item.amount.toFixed(2)}
      </Text>
    </TouchableOpacity>
  );

  if (loading && transactions.length === 0) {
    return (
      <View style={styles.centerContainer}>
        <ActivityIndicator size="large" color="#3498db" />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Dashboard</Text>

      <View style={styles.summaryContainer}>
        <View style={styles.summaryCard}>
          <Text style={styles.summaryLabel}>Income</Text>
          <Text style={styles.summaryValuePositive}>
            ${totalIncome.toFixed(2)}
          </Text>
        </View>
        <View style={styles.summaryCard}>
          <Text style={styles.summaryLabel}>Expense</Text>
          <Text style={styles.summaryValueNegative}>
            ${totalExpense.toFixed(2)}
          </Text>
        </View>
        <View style={styles.summaryCard}>
          <Text style={styles.summaryLabel}>Balance</Text>
          <Text
            style={[
              styles.summaryValue,
              {
                color: totalIncome - totalExpense >= 0 ? "#27ae60" : "#e74c3c",
              },
            ]}
          >
            ${(totalIncome - totalExpense).toFixed(2)}
          </Text>
        </View>
      </View>

      <Text style={styles.sectionTitle}>Recent Transactions</Text>

      <FlatList
        data={transactions}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderTransaction}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
        ListEmptyComponent={
          <Text style={styles.emptyText}>No transactions found</Text>
        }
      />

      <TouchableOpacity
        style={styles.fabButton}
        onPress={() => navigation.navigate("AddTransaction")}
      >
        <Text style={styles.fabButtonText}>+</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
    padding: 16,
  },
  centerContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  header: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 16,
    color: "#333",
  },
  summaryContainer: {
    flexDirection: "row",
    marginBottom: 24,
    gap: 12,
  },
  summaryCard: {
    flex: 1,
    backgroundColor: "#fff",
    padding: 12,
    borderRadius: 8,
    alignItems: "center",
    shadowColor: "#000",
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  summaryLabel: {
    fontSize: 12,
    color: "#666",
    marginBottom: 4,
  },
  summaryValue: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#333",
  },
  summaryValuePositive: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#27ae60",
  },
  summaryValueNegative: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#e74c3c",
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: "600",
    marginBottom: 12,
    color: "#333",
  },
  transactionCard: {
    backgroundColor: "#fff",
    padding: 14,
    borderRadius: 8,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 8,
    shadowColor: "#000",
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  transactionInfo: {
    flex: 1,
  },
  transactionTitle: {
    fontSize: 14,
    fontWeight: "600",
    color: "#333",
    marginBottom: 4,
  },
  transactionDate: {
    fontSize: 12,
    color: "#999",
  },
  transactionAmount: {
    fontSize: 14,
    fontWeight: "bold",
  },
  emptyText: {
    textAlign: "center",
    color: "#999",
    fontSize: 14,
    marginTop: 20,
  },
  fabButton: {
    position: "absolute",
    bottom: 20,
    right: 20,
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: "#3498db",
    justifyContent: "center",
    alignItems: "center",
    shadowColor: "#000",
    shadowOpacity: 0.3,
    shadowRadius: 6,
    elevation: 6,
  },
  fabButtonText: {
    fontSize: 32,
    color: "#fff",
    fontWeight: "bold",
  },
});
