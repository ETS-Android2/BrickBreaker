package com.richikin.gdxutils.maths;

public class ItemF implements IItem
{
    protected float maximum;
    protected float minimum;
    protected float total;
    protected float refillAmount;

    public ItemF()
    {
        this(0, 100, 0);
    }

    public ItemF(float minimum, float maximum)
    {
        this(minimum, maximum, minimum);
    }

    public ItemF(float maximum)
    {
        this(0, maximum, 0);
    }

    public ItemF(float minimum, float maximum, float total)
    {
        this.minimum      = minimum;
        this.maximum      = maximum;
        this.total        = total;
        this.refillAmount = minimum;
    }

    public float getTotal()
    {
        this.validate();

        return this.total;
    }

    public void setTotal(float amount)
    {
        this.total = amount;
    }

    public float getMin()
    {
        return minimum;
    }

    public void setMin(float minimum)
    {
        this.minimum = minimum;
    }

    public float getMax()
    {
        return maximum;
    }

    public void setMax(float maximum)
    {
        this.maximum = maximum;
    }

    public void add(float amount)
    {
        if ((this.total += amount) < 0)
        {
            this.total = 0;
        }
        else
        {
            if (this.total > this.maximum)
            {
                this.total = this.maximum;
            }
        }
    }

    public void add(float amount, float wrap)
    {
        if ((this.total += amount) > wrap)
        {
            this.total = this.minimum;
        }
    }

    public void subtract(float amount)
    {
        this.total = Math.max((this.total - amount), this.minimum);
    }

    public void subtract(float amount, float wrap)
    {
        if ((this.total -= amount) < this.minimum)
        {
            this.total = wrap;
        }
    }

    public void setMinMax(float minimum, float maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public void setToMaximum()
    {
        this.total = this.maximum;
    }

    public void setToMinimum()
    {
        this.total = this.minimum;
    }

    public boolean isFull()
    {
        return (this.total >= this.maximum);
    }

    public boolean isEmpty()
    {
        return (this.total <= this.minimum);
    }

    public boolean hasRoom()
    {
        return !isFull();
    }

    public boolean isOverflowing()
    {
        return this.total > this.maximum;
    }

    public boolean isUnderflowing()
    {
        return this.total < this.minimum;
    }

    public void refill()
    {
        this.total = this.refillAmount;
    }

    public void refill(float refillAmount)
    {
        this.total = refillAmount;
    }

    public float getRefillAmount()
    {
        return this.refillAmount;
    }

    public void setRefillAmount(float refill)
    {
        this.refillAmount = refill;
    }

    protected void validate()
    {
        if (this.total < this.minimum)
        {
            this.total = this.minimum;
        }

        if (this.total > this.maximum)
        {
            this.total = this.maximum;
        }
    }
}
