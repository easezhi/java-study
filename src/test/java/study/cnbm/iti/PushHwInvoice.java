package study.cnbm.iti;

import org.junit.Test;
import study.cnbm.iti.model.HwInvoice;
import study.cnbm.iti.model.HwInvoiceItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PushHwInvoice {

    @Test
    public void pushInvoice() {
        List<HwInvoice> invoiceList = new ArrayList<>();
        List<HwInvoiceItem> itemList = new ArrayList<>();
        var noItemList = new ArrayList<HwInvoice>();
        var itemGroup = itemList.stream().collect(Collectors.groupingBy(HwInvoiceItem::getInvoiceNumber));
        invoiceList.forEach(invoice -> {
            var items = itemGroup.get(invoice.getInvoiceNumber());
            if (items == null) {
                noItemList.add(invoice);
            } else {
                invoice.setItemList(items);
            }
        });
    }
}
