using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings.Types;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;

namespace Citylogia.Server.Core.Db
{
    public static class Configurations
    {
        public static IServiceCollection AddDb(this IServiceCollection services, IConnectionStrings connectionStrings)
        {
            services.AddDbContext<SqlContext>(options => options.UseNpgsql(connectionStrings.Postgres), ServiceLifetime.Transient);

            return services;
        }
    }
}
