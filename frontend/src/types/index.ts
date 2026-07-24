export interface Customer {
  id: number;
  name: string;
  email: string;
  phone: string;
  role: string;
  createdAt: string;
  token: string;
  accessKey: string;
  priority: "emergency" | "high" | "normal";
  isElderly: boolean;
  isEmergency: boolean;
  hasDisability: boolean;
  position: number | null;
  eta: number | null;
  status: "waiting" | "called" | "served" | "no-show";
  joinedAt: string;
  calledAt: string | null;
  servedAt: string | null;
}

export interface Service {
  id: number;
  name: string;
  description: string;
  estimatedDuration: number;
  isActive: boolean;
  createdAt: string;
}

export interface Staff {
  id: number;
  name: string;
  role: string;
  phone: string;
  email: string;
  employeeId: string;
  department: string;
  counterNumber: number;
  createdAt: string;
}

export interface Analytics {
  totalCustomer: number;
  waitingCustomer: number;
  servedToday: number;
  serviceDistribution: Record<string, number>;
  averageWaitTime: number;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  token: string;
  role: "STAFF" | "ADMIN";
  name: string;
  staffId?: number;
  adminId?: number;
}

export interface ApiResponse<T = any> {
  success: boolean;
  message?: string;
  data?: T;
}
