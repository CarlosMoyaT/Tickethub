export interface BookingRequest {
    userId: string;
    eventId: number;
    ticketCount: number;
}

export interface BookingResponse {
    userId: string;
    eventId: number;
    ticketCount: number;
    totalPrice: number;
}