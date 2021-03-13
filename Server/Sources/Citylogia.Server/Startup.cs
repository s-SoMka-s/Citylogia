using Autofac;
using Autofac.Extensions.DependencyInjection;
using Citylogia.Server.Core.Api.Tools;
using Citylogia.Server.Core.Db;
using Citylogia.Server.Core.Tools.Implementations.AppSettings;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings;
using Citylogia.Server.Initializations;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

namespace Citylogia.Server
{
    public class Startup
    {
        private readonly IAppSettings appSettings;

        public Startup(IConfiguration configuration)
        {
            appSettings = new AppSettings(configuration);
        }

        // This method gets called by the runtime. Use this method to add services to the container.
        // For more information on how to configure your application, visit https://go.microsoft.com/fwlink/?LinkID=398940
        public void ConfigureServices(IServiceCollection services)
        {
            Injections.AddServices(services, appSettings);
        }

        public void ConfigureContainer(ContainerBuilder builder)
        {
            Injections.AddServices(builder, appSettings);
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostApplicationLifetime appLifetime)
        {
            app.MigrateDb();
            app.UseApi();


            var applicationContainer = app.ApplicationServices.GetAutofacRoot();
            appLifetime.ApplicationStopped.Register(() =>
            {
                applicationContainer.Dispose();
            });
        }
    }
}
