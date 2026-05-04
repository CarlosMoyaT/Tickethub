export interface Order {
    id: number;
    customerId: number;
    eventId: number;
    ticketCount: number;
    totalPrice: number;
    placedAt: string;
}

export enum OrderStatus {
    PENDING = 'PENDING',
    PROCESSING = 'PROCESSING',
    COMPLETED = 'COMPLETED',
    CANCELLED = 'CANCELLED'
}