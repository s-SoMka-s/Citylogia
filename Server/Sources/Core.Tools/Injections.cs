using Autofac;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings.Types;
using Microsoft.Extensions.DependencyInjection;

namespace Citylogia.Server.Core.Tools
{
    public static class Injections
    {
        public static ContainerBuilder AddTools(this ContainerBuilder builder, IAppSettings settings)
        {
            builder.Register(_ => settings);
            builder.Register(_ => settings.ConnectionStrings).As<IConnectionStrings>();

            return builder;
        }

        public static IServiceCollection AddTools(this IServiceCollection services, IAppSettings settings)
        {
            //services.AddHttpContextAccessor();

            return services;
        }
    }
}
