namespace Libraries.Db.Base
{
    public interface IDataType
    {
        long Id { get; set; }
        bool Deleted { get; set; }
    }
}
