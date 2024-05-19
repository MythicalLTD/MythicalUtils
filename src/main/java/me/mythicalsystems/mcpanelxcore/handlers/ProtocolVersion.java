package me.mythicalsystems.mcpanelxcore.handlers;

public class ProtocolVersion
{
    public int id;
    public String name;
    
    // Constructors
    
    public ProtocolVersion()
    {
        id = -1;
        name = "UNKNOWN";
    }
    
    public ProtocolVersion(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    // Public methods
    
    public int getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String toString()
    {
        return name + "(" + id + ")";
    }
    
}