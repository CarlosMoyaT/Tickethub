export interface BookingRequest {
    userId: number;
    eventId: number;
    ticketCount: number;
}

export interface BookingResponse {
    userId: number;
    eventId: number;
    ticketCount: number;
    totalPrice: number;
}
