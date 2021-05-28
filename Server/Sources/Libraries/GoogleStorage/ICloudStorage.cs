using System;
using System.Threading.Tasks;

namespace Libraries.GoogleStorage
{
    public interface ICloudStorage
    {
        Task<string> UploadFileAsync(String imageFile, string fileNameForStorage);
        Task DeleteFileAsync(string fileNameForStorage);
    }
}
