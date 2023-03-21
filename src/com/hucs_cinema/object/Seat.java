package com.hucs_cinema.object;

public class Seat {
    private final Hall SEAT_OF_HALL; // Hall that seat belongs to
    private final int ROW; // Seat's row placement
    private final int COLUMN; // Seat's column placement
    private User OWNER; // Owner of the seat
    private int PRICE; // Seat's bought price

    /**
     * Constructor that creates new seat for given parameters
     * 
     * @param seatOfHall Hall that seat belongs to
     * @param row        Seat's row placement
     * @param column     Seat's column placement
     * @param owner      Owner of the seat
     * @param price      Seat's bought price
     */
    public Seat(Hall seatOfHall, int row, int column, User owner, int price) {
        this.SEAT_OF_HALL = seatOfHall;
        this.ROW = row;
        this.COLUMN = column;
        this.OWNER = owner;
        this.PRICE = price;
    }

    /**
     * @return Row of the seat
     */
    public int getRow() {
        return ROW;
    }

    /**
     * @return Column of the seat
     */
    public int getColumn() {
        return COLUMN;
    }

    /**
     * @return Owner of the seat
     */
    public User getOwner() {
        return OWNER;
    }

    /**
     * Set the seat's owner
     * 
     * @param owner New owner of the seat
     */
    public void setOwner(User owner) {
        this.OWNER = owner;
    }

    /**
     * @return Seat's bought price
     */
    public int getPrice() {
        return PRICE;
    }

    /**
     * Set the seat's bought price
     * 
     * @param price New bought price of the seat
     */
    public void setPrice(int price) {
        this.PRICE = price;
    }

    /**
     * Override because it's used in the load method
     * 
     * @return Properties of the seat in accordance with the format in the
     *         "backup.dat"
     */
    @Override
    public String toString() {
        if (this.getOwner() == null) {
            return "seat\t" + SEAT_OF_HALL.getFilmOfHall().getName() + "\t" + SEAT_OF_HALL.getName() + "\t" + ROW
                    + "\t" + COLUMN + "\tnull\t" + PRICE + "\n";
        } else {
            return "seat\t" + SEAT_OF_HALL.getFilmOfHall().getName() + "\t" + SEAT_OF_HALL.getName() + "\t" + ROW
                    + "\t" + COLUMN + "\t" + OWNER.getUsername() + "\t" + PRICE + "\n";
        }
    }
}
