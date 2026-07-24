package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

/**
 * E5 - Ket qua tinh HOAN TIEN khi huy ve.
 * Dung ca cho man hinh xac nhan huy (xem truoc so tien duoc hoan) va sau khi huy xong.
 *
 * @param totalPaid    tong tien da thanh toan cua ve
 * @param refundPercent ty le hoan (%) theo chinh sach
 * @param refundAmount  so tien thuc nhan lai
 * @param policyNote    giai thich ngan cho khach hieu vi sao duoc hoan bay nhieu
 */
public record RefundViewModel(
        double totalPaid,
        int refundPercent,
        double refundAmount,
        String policyNote
) {}
