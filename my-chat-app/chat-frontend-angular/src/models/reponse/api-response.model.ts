export interface ApiResponse<T> {
  data: T | null; // Generic type for the response data, can be null
  message: string; // Message string from the response
}
