## [Order] Create Order

### Endpoint
- **POST** `/api/orders`

### Description
- 새로운 주문을 생성하고 단일 거래 내에서 **자동적으로** 재고를 감소시킵니다.

### Request Body (JSON)
```json
{
  "customerId": 1,
  "items": [
    { "productId": 101, "quantity": 2 },
    { "productId": 205, "quantity": 1 }
  ]
}