using Google.Apis.Auth.OAuth2;
using Google.Cloud.Storage.V1;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Drawing;

namespace Libraries.GoogleStorage
{
    public class GoogleCloudStorage : ICloudStorage
    {
        private readonly GoogleCredential googleCredential;
        private readonly StorageClient storageClient;
        private readonly string bucketName;

        public GoogleCloudStorage(IConfiguration configuration)
        {
            googleCredential = GoogleCredential.FromFile(configuration.GetSection("GoogleCredentialFile").Value);
            storageClient = StorageClient.Create(googleCredential);
            bucketName = configuration.GetSection("GoogleCloudStorageBucket").Value;
        }

        async Task ICloudStorage.DeleteFileAsync(string fileNameForStorage)
        {
            await storageClient.DeleteObjectAsync(bucketName, fileNameForStorage);
        }

        async Task<string> ICloudStorage.UploadFileAsync(string base64File, string fileNameForStorage)
        {

            var bytes = ParseBase64(base64File);

            using var memoryStream = new MemoryStream(bytes);
            var img = System.Drawing.Image.FromStream(memoryStream);
            var dataObject = await storageClient.UploadObjectAsync(bucketName, fileNameForStorage, null, memoryStream);

            return dataObject.MediaLink;
        }

        private byte[] ParseBase64(string rawContent)
        {
            var content = rawContent.Trim();
            var base64 = content;

            if (content.StartsWith("data:"))
            {
                var groups = Regex.Match(rawContent, @"data:(?<type>.+?)/(?<extension>.+?),(?<data>.+)");
                base64 = groups.Groups["data"].Value;
            }

            return Convert.FromBase64String(base64);
        }
    }
}
