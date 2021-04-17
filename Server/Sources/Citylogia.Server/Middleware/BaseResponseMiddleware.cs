using Core.Api.Models;
using Microsoft.AspNetCore.Http;
using System.IO;
using System.Text;
using System.Threading.Tasks;

namespace Citylogia.Server.Middleware
{
    public class BaseResponseMiddleware
    {
        private readonly RequestDelegate _next;

        public BaseResponseMiddleware(RequestDelegate next)
        {
            this._next = next;
        }

        public void Invoke(HttpContext context)
        {
            var body = context.Response.Body;

            if (!body.CanRead)
            {
                _next.Invoke(context);
            }
            
            var reader = new StreamReader(body);
            string stringBody = reader.ReadToEnd();

            stringBody = new BaseApiResponse<string>(200, stringBody).ToString();

            var byteArray = Encoding.ASCII.GetBytes(stringBody);
            context.Response.Body = new MemoryStream(byteArray);

           _next.Invoke(context);
        }
    }
}
