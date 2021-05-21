﻿using Core.Api.Models;
using Microsoft.AspNetCore.Http;
using System;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Microsoft.Extensions.Logging;

namespace Citylogia.Server.Middleware
{
    public class BaseResponseMiddleware
    {
        private readonly RequestDelegate nextAsync;
        private readonly ILogger logger;

        public BaseResponseMiddleware(RequestDelegate nextAsync, ILoggerFactory loggerFactory)
        {
            this.nextAsync = nextAsync;
            this.logger = loggerFactory.CreateLogger<BaseResponseMiddleware>();
        }

        public async Task InvokeAsync(HttpContext context)
        {
            using var stream = new MemoryStream();
            using var reader = new StreamReader(stream, Encoding.Default);

            var defaultBody = context.Response.Body;

            context.Response.Body = stream;

            await nextAsync(context);

            reader.BaseStream.Seek(0, SeekOrigin.Begin);
            // content - body от контроллера
            var content = await reader.ReadToEndAsync();
            var obj = JsonConvert.DeserializeObject(content);
            var @new = new BaseApiResponse<object>(200, obj);

            var response = JsonConvert.SerializeObject(@new);
            await CompleteResponseAsync(context, response, defaultBody);

            this.logger.LogInformation(
            "Request {method} {url} => {statusCode}",
            context.Request?.Method,
            context.Request?.Path.Value,
            context.Response?.StatusCode);
        }

        private async Task CompleteResponseAsync(HttpContext context, string response, Stream defaultBody)
        {
            context.Response.Body = defaultBody;
            context.Response.ContentType = "application/json";
            context.Response.Headers.Remove("Content-Length");

            await context.Response.WriteAsync(response);
        }

    }
}
