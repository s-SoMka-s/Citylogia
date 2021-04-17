
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;

namespace Libraries.Db.Reposiitory.Interfaces
{
    public interface IRepository<T>
    {
        DbContext Context { get; }

        Task<T> AddAsync(T entity);
    }
}
