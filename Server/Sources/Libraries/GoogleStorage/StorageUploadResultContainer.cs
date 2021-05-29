namespace Libraries.GoogleStorage
{
    public class StorageUploadResultContainer
    {
        public StorageUploadResultContainer(string url, string name)
        {
            PublicUrl = url;
            Name = name;
        }

        public string PublicUrl { get; set; }
        public string Name { get; set; }
    }
}
