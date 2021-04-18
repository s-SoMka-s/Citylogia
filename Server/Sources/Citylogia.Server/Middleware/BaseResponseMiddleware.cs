using Core.Api.Models;
using Microsoft.AspNetCore.Http;
using System;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace Citylogia.Server.Middleware
{
    public class BaseResponseMiddleware
    {
        private readonly RequestDelegate nextAsync;

        public BaseResponseMiddleware(RequestDelegate nextAsync)
        {
            this.nextAsync = nextAsync;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            var watch = new Stopwatch();
            watch.Start();

            //To add Headers AFTER everything you need to do this
            context.Response.OnStarting(state => {
                var httpContext = (HttpContext)state;
                httpContext.Response.ContentType = "application/json";
                httpContext.Response.Headers.Remove("Content-Length");

                return Task.CompletedTask;
            }, context);

            var newContent = string.Empty;

            using (var newBody = new MemoryStream())
            {
                // We set the response body to our stream so we can read after the chain of middlewares have been called.
                //context.Response.Body = newBody;

                await nextAsync(context);

                // Reset the body so nothing from the latter middlewares goes to the output.
               context.Response.Body = new MemoryStream();

                newBody.Seek(0, SeekOrigin.Begin);

                newContent += ", World!";

                // Send our modified content to the response body.
                await context.Response.WriteAsync(newContent);
            }
        }
    }
}
