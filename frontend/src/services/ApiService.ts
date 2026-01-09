import axios, { AxiosInstance, AxiosError } from "axios";

const API_BASE_URL = "http://localhost:8080/api";

let authToken: string | null = null;

const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Add token to requests
apiClient.interceptors.request.use(
  (config) => {
    if (authToken) {
      config.headers.Authorization = `Bearer ${authToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Handle responses
apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      // Handle unauthorized
      authToken = null;
      // Emit event to logout or refresh token
    }
    return Promise.reject(error);
  }
);

export const AuthService = {
  login: async (email: string, password: string) => {
    const response = await apiClient.post("/auth/login", { email, password });
    authToken = response.data.token;
    return response.data;
  },

  register: async (
    email: string,
    password: string,
    firstName: string,
    lastName: string
  ) => {
    const response = await apiClient.post("/auth/register", {
      email,
      password,
      firstName,
      lastName,
      currency: "USD",
    });
    return response.data;
  },

  setToken: (token: string) => {
    authToken = token;
  },

  getToken: () => authToken,

  logout: () => {
    authToken = null;
  },
};

export const TransactionService = {
  getTransactions: async (page: number = 0, size: number = 10) => {
    return apiClient.get(`/transactions?page=${page}&size=${size}`);
  },

  getTransactionById: async (id: number) => {
    return apiClient.get(`/transactions/${id}`);
  },

  createTransaction: async (data: any) => {
    return apiClient.post("/transactions", data);
  },

  updateTransaction: async (id: number, data: any) => {
    return apiClient.put(`/transactions/${id}`, data);
  },

  deleteTransaction: async (id: number) => {
    return apiClient.delete(`/transactions/${id}`);
  },

  getTransactionsByDateRange: async (startDate: string, endDate: string) => {
    return apiClient.get(
      `/transactions/range?start=${startDate}&end=${endDate}`
    );
  },
};

export const CategoryService = {
  getCategories: async () => {
    return apiClient.get("/categories");
  },

  getCategoryById: async (id: number) => {
    return apiClient.get(`/categories/${id}`);
  },

  createCategory: async (data: any) => {
    return apiClient.post("/categories", data);
  },

  updateCategory: async (id: number, data: any) => {
    return apiClient.put(`/categories/${id}`, data);
  },

  deleteCategory: async (id: number) => {
    return apiClient.delete(`/categories/${id}`);
  },
};

export const UserService = {
  getProfile: async () => {
    return apiClient.get("/users/profile");
  },

  updateProfile: async (data: any) => {
    return apiClient.put("/users/profile", data);
  },

  changePassword: async (oldPassword: string, newPassword: string) => {
    return apiClient.post("/users/change-password", {
      oldPassword,
      newPassword,
    });
  },
};

export default apiClient;
