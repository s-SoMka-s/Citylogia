using System.Threading.Tasks;

namespace Libraries.GoogleStorage
{
    public interface ICloudStorage
    {
        Task<string> UploadFileAsync(string name, string extension, string content);
        Task DeleteFileAsync(string name);
    }
}
