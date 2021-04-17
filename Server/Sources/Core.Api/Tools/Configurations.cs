using Microsoft.AspNetCore.Builder;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings;
using Microsoft.Extensions.DependencyInjection;

namespace Citylogia.Server.Core.Api.Tools
{
    public static class Configurations
    {
        public static IServiceCollection AddApi(
             this IServiceCollection services,
             IAppSettings settings)
        {
            services.AddSession();

            services.AddCors();
            services.AddMvc(options =>
            {
                options.EnableEndpointRouting = false;
            })
                .AddNewtonsoftJson()
                .AddControllersAsServices();

            return services;
        }

        public static void UseApi(this IApplicationBuilder app)
        {
            app.UseCors(builder => builder.AllowAnyOrigin()
                                          .AllowAnyHeader()
                                          .AllowAnyMethod());

            app.UseHttpMethodOverride();

            app.UseSession();
            app.UseMvc();

            app.UseRouting();
        }
    }
}
