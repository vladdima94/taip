package Utils;

public class UriUtils  {
    private String originalURI;
    private String [] params;
    
    
    public UriUtils(String originalURI)
    {
        this.originalURI = originalURI;
        if(this.originalURI != null)
        {
            this.params = this.originalURI.split("/");
        }
    }
    
    public String getController()        
    {
        if(params.length > 2)
        {
            return this.params[2];
        }
        return null;
    }
    public String getSubController()
    {
        if(params.length > 3)
        {
            return this.params[3];
        }
        return null;
    }       
}
