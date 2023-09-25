package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    private HashMap<String,Order> Orderlist;
    private HashMap<String,DeliveryPartner> Partnerlist;
    private HashMap<String, List<String>> PartnerOrderlist;
    private HashMap<String,String> OrderPartnerlist;

    private int OrdersassignedToPartners;
    public OrderRepository()
    {
        Orderlist = new HashMap<>();
        Partnerlist = new HashMap<>();
        PartnerOrderlist = new HashMap<>();
        OrderPartnerlist = new HashMap<>();
        OrdersassignedToPartners = 0;
    }

    public void addOrder(Order order) {
        Orderlist.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        PartnerOrderlist.put(partnerId,new ArrayList<>());
        Partnerlist.put(partnerId,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {

        if(Orderlist.containsKey(orderId) && Partnerlist.containsKey(partnerId))
        {
            PartnerOrderlist.get(partnerId).add(orderId);
            Partnerlist.get(partnerId).setNumberOfOrders(Partnerlist.get(partnerId).getNumberOfOrders()+1);

            OrderPartnerlist.put(orderId,partnerId);

            OrdersassignedToPartners++;
        }
    }

    public Order getOrderById(String orderId) {
        return Orderlist.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return Partnerlist.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return Partnerlist.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return PartnerOrderlist.get(partnerId);
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(Orderlist.keySet());
    }

    public Integer getCountOfUnassignedOrders() {
        return Orderlist.size() - OrdersassignedToPartners;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {

        Integer orderleft = 0;

        int timetaken = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));
        for(String orderId : PartnerOrderlist.get(partnerId))
        {
            if(Orderlist.get(orderId).getDeliveryTime() > timetaken)
            {
                orderleft++;
            }
        }

        return orderleft;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {

        int Last_time = 0;
        for(String orders : PartnerOrderlist.get(partnerId))
        {
            int time = Orderlist.get(orders).getDeliveryTime();
            Last_time = Math.max(time,Last_time);
        }

        return Last_time;
    }

    public void deletePartnerById(String partnerId) {
        int no_of_Orders = Partnerlist.get(partnerId).getNumberOfOrders();
        OrdersassignedToPartners -= no_of_Orders;
        PartnerOrderlist.remove(partnerId);
        Partnerlist.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {

        if(OrderPartnerlist.containsKey(orderId))
        {
            String PartnerId = OrderPartnerlist.get(orderId);
            OrderPartnerlist.remove(orderId);
            for(String Orders : PartnerOrderlist.get(PartnerId))
            {
                if(Orders == orderId)
                {
                    PartnerOrderlist.get(Orders).remove(Orders);
                    break;
                }
            }

            Partnerlist.get(PartnerId).setNumberOfOrders(Partnerlist.get(PartnerId).getNumberOfOrders() - 1);
            OrdersassignedToPartners--;
        }
        Orderlist.remove(orderId);
    }
}
