export type Priority = "normal" | "high" | "emergency";
export type CustomerStatus = "waiting" | "called" | "served" | "no-show";
export type Role = "ADMIN" | "STAFF";

export interface Customer {
  id: number;
  name: string;
  email: string;
  phone: string;
  tokenNumber: string;
  accessKey: string;
  priority: Priority;
  position: number | null;
  eta: number | null;
  status: CustomerStatus;
  joinedAt: string;
  calledAt: string | null;
  servedAt: string | null;
}

export interface ServiceItem {
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
  email: string;
  phone: string;
  employeeId: string;
  department: string;
  counterNumber: number;
}

export interface Analytics {
  totalCustomers: number;
  waitingCustomers: number;
  servedToday: number;
  serviceDistribution: Record<string, number>;
  averageWaitTime: number;
}

export interface AuthUser {
  token: string;
  refreshToken: string;
  role: Role;
  name: string;
  id: number;
}

export interface QueueUpdateMessage {
  serviceType: string;
  queueSize: number;
  customers: Customer[];
  timestamp: number;
}

export interface CustomerUpdateMessage {
  customerId: number;
  tokenNumber: string;
  position: number | null;
  eta: number | null;
  status: CustomerStatus;
  timestamp: number;
}
