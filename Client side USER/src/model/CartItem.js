export class CartItem {

    constructor(vendor, itemName, amount){
        this.vendor = vendor;
        this.itemName = itemName;
        this.amount = Number(amount);
        this.price = Number(null);
    }

}