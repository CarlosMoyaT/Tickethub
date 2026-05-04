import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { OrderService } from '../../services/order.service';
import { Order, OrderStatus } from '../../models/order.model';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatChipsModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit {
  private orderService = inject(OrderService);

  orders = signal<Order[]>([]);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar las órdenes. Por favor, intenta de nuevo.');
        this.loading.set(false);
        console.error('Error loading orders:', err);
      }
    });
  }

  // Order status is not returned by backend currently
  // These methods are kept for future use
  getStatusClass(status: OrderStatus): string {
    switch (status) {
      case OrderStatus.COMPLETED:
        return 'completed';
      case OrderStatus.PROCESSING:
        return 'processing';
      case OrderStatus.PENDING:
        return 'pending';
      case OrderStatus.CANCELLED:
        return 'cancelled';
      default:
        return '';
    }
  }

  getStatusText(status: OrderStatus): string {
    switch (status) {
      case OrderStatus.COMPLETED:
        return 'Completada';
      case OrderStatus.PROCESSING:
        return 'En Proceso';
      case OrderStatus.PENDING:
        return 'Pendiente';
      case OrderStatus.CANCELLED:
        return 'Cancelada';
      default:
        return status;
    }
  }
}